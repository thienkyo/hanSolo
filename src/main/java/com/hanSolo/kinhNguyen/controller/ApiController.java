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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("getQueueSms")
    public QueueSmsResponse getQueueSms() throws ParseException {
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

    @RequestMapping("getSmsStatus")
    public String getSmsStatus(@RequestParam String id) throws ParseException {
        Optional<SmsQueue> oneSmsOpt = smsQueueRepo.findById(Integer.parseInt(id));
        if(oneSmsOpt.isPresent()){
            SmsQueue oneSms = oneSmsOpt.get();

            Optional<SmsJob> jobOpt = smsJobRepo.findById(oneSms.getJobId());
            if(jobOpt.isPresent()){
                SmsJob job = jobOpt.get();
                if(Utility.SMS_JOB_COMMON.equals(job.getJobType())){
                    Optional<SmsUserInfo> smsUserInfoOpt = smsUserInfoRepo.findByPhone(oneSms.getReceiverPhone());
                    if(smsUserInfoOpt.isPresent()){
                        SmsUserInfo smsUserInfo = smsUserInfoOpt.get();
                        smsUserInfo.setLastSendSmsDate(Utility.getCurrentDate());
                        smsUserInfoRepo.save(smsUserInfo);
                    }
                }else if(Utility.SMS_JOB_SPECIFIC.equals(job.getJobType())){
                    Optional<SpecificSmsUserInfo> specSmsUserInfoOpt = specificSmsUserInfoRepo.findByPhone(oneSms.getReceiverPhone());
                    if(specSmsUserInfoOpt.isPresent()){
                        SpecificSmsUserInfo specSmsUserInfo = specSmsUserInfoOpt.get();
                        specSmsUserInfo.setLastSendSmsDate(Utility.getCurrentDate());
                        specificSmsUserInfoRepo.save(specSmsUserInfo);
                    }
                }
            }

            oneSms.setStatus(Utility.SMS_QUEUE_SENT);
            oneSms.setGmtModify(Utility.getCurrentDate());
            smsQueueRepo.save(oneSms);
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

        List<SmsQueue> smsQueueSending = smsQueueRepo.findFirst100ByStatusOrderByGmtCreateAsc(Utility.SMS_QUEUE_SENDING);
        smsQueueList.addAll(smsQueueSending);
        smsQueueRepo.saveAll(smsQueueList);
        CommonCache.LAST_PREPARE_DATA_HEARTBEAT_TIME = Utility.getCurrentDate();
        return null;
    }

    private SmsQueue generateSmsQueue(SmsJob job, SmsUserInfo smsUserInfo) throws ParseException {
        SmsQueue smsQueue = new SmsQueue();
        smsQueue.setJobId(job.getId());
        smsQueue.setGmtCreate(Utility.getCurrentDate());
        smsQueue.setGmtModify(Utility.getCurrentDate());
        smsQueue.setContent(job.getMsgContentTemplate());
        smsQueue.setGender(smsUserInfo.getGender());
        smsQueue.setStatus(Utility.SMS_QUEUE_INIT);
        smsQueue.setReceiverName(smsUserInfo.getName());
        smsQueue.setReceiverPhone(smsUserInfo.getPhone());
        smsQueue.setWeight(job.getWeight());

        return smsQueue;
    }

    private SmsQueue generateSmsQueue2(SmsJob job, SpecificSmsUserInfo smsUserInfo) throws ParseException {
        SmsQueue smsQueue = new SmsQueue();
        smsQueue.setJobId(job.getId());
        smsQueue.setGmtCreate(Utility.getCurrentDate());
        smsQueue.setGmtModify(Utility.getCurrentDate());
        smsQueue.setContent(job.getMsgContentTemplate());
        smsQueue.setGender(smsUserInfo.getGender());
        smsQueue.setStatus(Utility.SMS_QUEUE_INIT);
        smsQueue.setReceiverName(smsUserInfo.getName());
        smsQueue.setReceiverPhone(smsUserInfo.getPhone());
        smsQueue.setWeight(job.getWeight());

    return smsQueue;
    }

    private SmsQueue generateSmsQueue3(SmsJob job, String phone) throws ParseException {
        SmsQueue smsQueue = new SmsQueue();
        smsQueue.setJobId(job.getId());
        smsQueue.setGmtCreate(Utility.getCurrentDate());
        smsQueue.setGmtModify(Utility.getCurrentDate());
        smsQueue.setContent(job.getMsgContentTemplate());
        smsQueue.setStatus(Utility.SMS_QUEUE_INIT);
        smsQueue.setReceiverPhone(phone);
        smsQueue.setWeight(job.getWeight());

        return smsQueue;
    }
}
