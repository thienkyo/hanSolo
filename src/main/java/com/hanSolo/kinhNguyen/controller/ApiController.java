package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.cacheCenter.CommonCache;
import com.hanSolo.kinhNguyen.models.SmsJob;
import com.hanSolo.kinhNguyen.models.SmsQueue;
import com.hanSolo.kinhNguyen.models.SmsUserInfo;
import com.hanSolo.kinhNguyen.models.SpecificSmsUserInfo;
import com.hanSolo.kinhNguyen.repository.SmsJobRepository;
import com.hanSolo.kinhNguyen.repository.SmsQueueRepository;
import com.hanSolo.kinhNguyen.repository.SmsUserInfoRepository;
import com.hanSolo.kinhNguyen.repository.SpecificSmsUserInfoRepository;
import com.hanSolo.kinhNguyen.response.QueueSmsResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired private SmsQueueRepository smsQueueRepo;
    @Autowired private SmsUserInfoRepository smsUserInfoRepo;
    @Autowired private SmsJobRepository smsJobRepo;
    @Autowired private SpecificSmsUserInfoRepository specificSmsUserInfoRepo;

    @RequestMapping(value = "getQueueSms2/{clientCode}/", method = RequestMethod.GET)
    public QueueSmsResponse getQueueSms2(@PathVariable final String clientCode) throws ParseException {
        if(!CommonCache.SMS_SEND_CONTROL){
            return null;
        }
        Optional<SmsQueue> smsQueueOpt = smsQueueRepo.findFirstByStatusOrderByWeightDescGmtCreateAsc(Utility.SMS_QUEUE_INIT);
        CommonCache.LAST_SMS_HEARTBEAT_TIME = Utility.getCurrentDate();
        if(smsQueueOpt.isPresent()){
            SmsQueue smsQueue = smsQueueOpt.get();
            smsQueue.setStatus(Utility.SMS_QUEUE_SENDING);
            smsQueue.setGmtModify(Utility.getCurrentDate());
            smsQueueRepo.save(smsQueue);
            return new QueueSmsResponse(smsQueue.getId().toString(),smsQueue.getReceiverPhone(),smsQueue.getContent());
        }

        return null;
    }

    @RequestMapping("getQueueSms")
    public QueueSmsResponse getQueueSms() throws ParseException {
        if(!CommonCache.SMS_SEND_CONTROL){
            return null;
        }
        Optional<SmsQueue> smsQueueOpt = smsQueueRepo.findFirstByStatusOrderByWeightDescGmtCreateAsc(Utility.SMS_QUEUE_INIT);
        CommonCache.LAST_SMS_HEARTBEAT_TIME = Utility.getCurrentDate();
        if(smsQueueOpt.isPresent()){
            SmsQueue smsQueue = smsQueueOpt.get();

            if(smsQueue.getJobType() == null || smsQueue.getJobType().equalsIgnoreCase(Utility.SMS_JOB_COMMON) || smsQueue.getJobType().equalsIgnoreCase("")){
                List<String> statuses = new ArrayList<>();
                statuses.add(Utility.SMS_QUEUE_SENDING);
                statuses.add(Utility.SMS_QUEUE_SENT);

                if( CommonCache.LAST_SENT_SMS == null){
                    Optional<SmsQueue> latestCommonSmsOpt = smsQueueRepo.findFirstByJobTypeAndStatusInOrderByGmtModifyDesc( Utility.SMS_JOB_COMMON, statuses);
                    CommonCache.LAST_SENT_SMS = latestCommonSmsOpt.get();
                }

                Calendar currentTime = Calendar.getInstance();
                System.out.println("getQueueSms api, currentTime:"+currentTime.toString());
                currentTime.add(Calendar.MINUTE, -2);// COMMON sms send sms with 2 min interval;
                System.out.println("getQueueSms api, currentTime minus 2min:"+ currentTime.toString());
                System.out.println("getQueueSms api, LAST_SENT_SMS:"+ CommonCache.LAST_SENT_SMS.getGmtModify().toString());

                if(CommonCache.LAST_SENT_SMS.getGmtModify().before(currentTime.getTime())){
                    smsQueue.setStatus(Utility.SMS_QUEUE_SENDING);
                    smsQueue.setGmtModify(Utility.getCurrentDate());
                    smsQueueRepo.save(smsQueue);
                    CommonCache.LAST_SENT_SMS = smsQueue;
                    return new QueueSmsResponse(smsQueue.getId().toString(),smsQueue.getReceiverPhone(),smsQueue.getContent());
                }
            }
        }

        return null;
    }

    @RequestMapping("getSmsStatus")
    public String getSmsStatus(@RequestParam String id) throws ParseException {
        Optional<SmsQueue> oneSmsOpt = smsQueueRepo.findById(Integer.parseInt(id));
        if(oneSmsOpt.isPresent()){
            SmsQueue oneSms = oneSmsOpt.get();
            oneSms.setStatus(Utility.SMS_QUEUE_SENT);
            oneSms.setGmtModify(Utility.getCurrentDate());
            smsQueueRepo.save(oneSms);

            if(Utility.SMS_JOB_COMMON.equals(oneSms.getJobType())){
                smsUserInfoRepo.updateLastSendSmsDateByPhone(Utility.getCurrentDate(), oneSms.getReceiverPhone());

            }else if(Utility.SMS_JOB_SPECIFIC.equals(oneSms.getJobType())){
                specificSmsUserInfoRepo.updateLastSendSmsDateByPhone(Utility.getCurrentDate(), oneSms.getReceiverPhone());
            }

            return "SUCCESS";
        }
        return "FAIL";
    }

    @RequestMapping("prepareSmsData")
    public QueueSmsResponse prepareSmsData() throws ParseException {
        if(!CommonCache.SMS_DATA_PREPARE_CONTROL){
            return null;
        }

        List<SmsJob> smsJobList = smsJobRepo.findByStatus(true);
        List<SmsQueue> smsQueueList = new ArrayList<>();
        for (SmsJob job : smsJobList) {
            Calendar calendarCreateOrder = Calendar.getInstance();
            Calendar calendarNoSmsDay = Calendar.getInstance();
            if(job.getIntervalTime() >= 1){
                calendarCreateOrder.add(Calendar.DAY_OF_MONTH, -job.getIntervalTime());
            }
            calendarNoSmsDay.add(Calendar.DAY_OF_MONTH, -job.getNoSmsDays());

            if(Utility.SMS_JOB_COMMON.equals(job.getJobType())){// for common case: apply to all user
                List<SmsUserInfo> smsUserInfos;
                if(null != job.getStartDate()){
                    smsUserInfos = job.getIsTest() ?
                            smsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndIsTestUserAndOrderCreateDateAfterOrderByOrderCreateDateAsc(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%",calendarNoSmsDay.getTime(),true,job.getStartDate())
                            : smsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndOrderCreateDateAfterOrderByOrderCreateDateAsc(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%", calendarNoSmsDay.getTime(), job.getStartDate());
                }else{
                    smsUserInfos = job.getIsTest() ?
                            smsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndIsTestUser(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%",calendarNoSmsDay.getTime(),true)
                            : smsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBefore(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%", calendarNoSmsDay.getTime());
                }

                for (SmsUserInfo smsUserInfo : smsUserInfos) {
                    smsUserInfo.setJobIdList(smsUserInfo.getJobIdList() + "," + job.getId() + "|");
                    smsUserInfo.setLastSendSmsDate(Utility.getCurrentDate());

                    SmsQueue smsQueue = generateSmsQueue(job,smsUserInfo);
                    smsQueueList.add(smsQueue);
                }
                smsUserInfoRepo.saveAll(smsUserInfos);
            }else if(Utility.SMS_JOB_SPECIFIC.equals(job.getJobType())){ // for recheck case
                List<SpecificSmsUserInfo> specSmsUserInfos = job.getIsTest() ?
                        specificSmsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndJobIdToRunAndIsTestUser(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%",calendarNoSmsDay.getTime(),job.getId().toString(),true)
                        : specificSmsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndJobIdToRun(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%", calendarNoSmsDay.getTime(),job.getId().toString());

                for (SpecificSmsUserInfo specificSmsUserInfo : specSmsUserInfos) {
                    specificSmsUserInfo.setJobIdList(specificSmsUserInfo.getJobIdList() + "," + job.getId() + "|");
                    specificSmsUserInfo.setLastSendSmsDate(Utility.getCurrentDate());

                    SmsQueue smsQueue = generateSmsQueue2(job,specificSmsUserInfo);
                    smsQueueList.add(smsQueue);
                }
                specificSmsUserInfoRepo.saveAll(specSmsUserInfos);
            }

            if(Utility.SMS_JOB_PARTICULAR.equals(job.getJobType())){ // for Specific Phones field
                if(!job.getSpecificPhones().isEmpty()){
                    List<String> phones = Arrays.asList(job.getSpecificPhones().split("\\s*,\\s*"));
                    for(String phone : phones){
                        SmsQueue smsQueue = generateSmsQueue3(job,phone);
                        smsQueueList.add(smsQueue);
                    }
                    job.setSpecificPhones("");
                    smsJobRepo.save(job);
                }
            }
        }

       // List<SmsQueue> smsQueueSending = smsQueueRepo.findFirst100ByStatusOrderByGmtCreateAsc(Utility.SMS_QUEUE_SENDING);
       // smsQueueList.addAll(smsQueueSending);
        smsQueueRepo.saveAll(smsQueueList);
        CommonCache.LAST_PREPARE_DATA_HEARTBEAT_TIME = Utility.getCurrentDate();
        return null;
    }

    private SmsQueue generateSmsQueue(SmsJob job, SmsUserInfo smsUserInfo) throws ParseException {
        SmsQueue smsQueue = new SmsQueue();
        smsQueue.setJobId(job.getId());
        smsQueue.setGmtCreate(Utility.getCurrentDate());
        smsQueue.setGmtModify(Utility.getCurrentDate());
        smsQueue.setContent(job.getMsgContentTemplate() +" "+ RandomStringUtils.randomAlphanumeric(4));
        smsQueue.setGender(smsUserInfo.getGender());
        smsQueue.setStatus(Utility.SMS_QUEUE_INIT);
        smsQueue.setReceiverName(smsUserInfo.getName());
        smsQueue.setReceiverPhone(smsUserInfo.getPhone());
        smsQueue.setWeight(job.getWeight());
        smsQueue.setJobType(job.getJobType());

        return smsQueue;
    }

    private SmsQueue generateSmsQueue2(SmsJob job, SpecificSmsUserInfo smsUserInfo) throws ParseException {
        SmsQueue smsQueue = new SmsQueue();
        smsQueue.setJobId(job.getId());
        smsQueue.setGmtCreate(Utility.getCurrentDate());
        smsQueue.setGmtModify(Utility.getCurrentDate());
        smsQueue.setContent(job.getMsgContentTemplate() +" "+ RandomStringUtils.randomAlphanumeric(4));
        smsQueue.setGender(smsUserInfo.getGender());
        smsQueue.setStatus(Utility.SMS_QUEUE_INIT);
        smsQueue.setReceiverName(smsUserInfo.getName());
        smsQueue.setReceiverPhone(smsUserInfo.getPhone());
        smsQueue.setWeight(job.getWeight());
        smsQueue.setJobType(job.getJobType());

    return smsQueue;
    }

    private SmsQueue generateSmsQueue3(SmsJob job, String phone) throws ParseException {
        SmsQueue smsQueue = new SmsQueue();
        smsQueue.setJobId(job.getId());
        smsQueue.setGmtCreate(Utility.getCurrentDate());
        smsQueue.setGmtModify(Utility.getCurrentDate());
        smsQueue.setContent(job.getMsgContentTemplate() +" "+ RandomStringUtils.randomAlphanumeric(4));
        smsQueue.setStatus(Utility.SMS_QUEUE_INIT);
        smsQueue.setReceiverPhone(phone);
        smsQueue.setWeight(job.getWeight());
        smsQueue.setJobType(job.getJobType());

        return smsQueue;
    }
}
