package com.hanSolo.kinhNguyen.controller;

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
        List<SmsJob> smsJobList = smsJobRepo.findByStatus(true);
        List<SmsQueue> smsQueueList = new ArrayList<>();

        Calendar calendarCreateOrder = Calendar.getInstance();
        Calendar calendarNoSmsDay = Calendar.getInstance();
        for (SmsJob job : smsJobList) {

            if(job.getIntervalTime() >= 1){
                calendarCreateOrder.add(Calendar.DAY_OF_MONTH, -job.getIntervalTime());
            }
            calendarNoSmsDay.add(Calendar.DAY_OF_MONTH, -job.getNoSmsDays());

            if(Utility.SMS_JOB_COMMON.equals(job.getJobType())){
                List<SmsUserInfo> smsUserInfos = job.getIsTest() ?
                        smsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndIsTestUser(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%",calendarNoSmsDay.getTime(),true)
                        : smsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBefore(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%", calendarNoSmsDay.getTime());

                for (SmsUserInfo smsUserInfo : smsUserInfos) {
                    smsUserInfo.setJobIdList(smsUserInfo.getJobIdList() + "," + job.getId() + "|");
                    smsUserInfo.setLastSendSmsDate(Utility.getCurrentDate());

                    SmsQueue smsQueue = generateSmsQueue(job,smsUserInfo);
                    smsQueueList.add(smsQueue);
                }
               // smsQueueRepo.saveAll(smsQueueList);
                smsUserInfoRepo.saveAll(smsUserInfos);
            }else if(Utility.SMS_JOB_SPECIFIC.equals(job.getJobType())){
                List<SpecificSmsUserInfo> specSmsUserInfos = job.getIsTest() ?
                        specificSmsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndJobIdToRunAndIsTestUser(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%",calendarNoSmsDay.getTime(),job.getId().toString(),true)
                        : specificSmsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndJobIdToRun(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%", calendarNoSmsDay.getTime(),job.getId().toString());

                for (SpecificSmsUserInfo specificSmsUserInfo : specSmsUserInfos) {
                    specificSmsUserInfo.setJobIdList(specificSmsUserInfo.getJobIdList() + "," + job.getId() + "|");
                    specificSmsUserInfo.setLastSendSmsDate(Utility.getCurrentDate());

                    SmsQueue smsQueue = generateSmsQueue2(job,specificSmsUserInfo);
                    smsQueueList.add(smsQueue);
                }

                if(!job.getSpecificPhones().isEmpty()){
                    List<String> phones = Arrays.asList(job.getSpecificPhones().split("\\s*,\\s*"));
                    for(String phone : phones){
                        SmsQueue smsQueue = generateSmsQueue3(job,phone);
                        smsQueueList.add(smsQueue);
                    }
                    job.setSpecificPhones("");
                    smsJobRepo.save(job);
                }
                specificSmsUserInfoRepo.saveAll(specSmsUserInfos);
            }
        }
        smsQueueRepo.saveAll(smsQueueList);

        Optional<SmsQueue> smsQueueOpt = smsQueueRepo.findFirstByStatusOrderByGmtCreateAsc(Utility.SMS_QUEUE_INIT);
        if(smsQueueOpt.isPresent()){
            SmsQueue smsQueue = smsQueueOpt.get();
            smsQueue.setStatus(Utility.SMS_QUEUE_SENDING);
            smsQueueRepo.save(smsQueue);
            return new QueueSmsResponse(smsQueue.getId().toString(),smsQueue.getReceiverPhone(),smsQueue.getContent());
        }
        return null;
    }

    @RequestMapping("getSmsStatus")
    public String getSmsStatus(@RequestParam String id) {
        Optional<SmsQueue> oneSmsOpt = smsQueueRepo.findById(Integer.parseInt(id));
        if(oneSmsOpt.isPresent()){
            SmsQueue oneSms = oneSmsOpt.get();
            oneSms.setStatus(Utility.SMS_QUEUE_SENT);
            smsQueueRepo.save(oneSms);
            return "SUCCESS";
        }
        return "FAIL";
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

        return smsQueue;
    }
}
