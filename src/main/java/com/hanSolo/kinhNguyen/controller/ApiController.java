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
import com.hanSolo.kinhNguyen.service.SmsService;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired private SmsQueueRepository smsQueueRepo;
    @Autowired private SmsUserInfoRepository smsUserInfoRepo;
    @Autowired private SmsJobRepository smsJobRepo;
    @Autowired private SpecificSmsUserInfoRepository specificSmsUserInfoRepo;

    @Autowired private SmsService smsService;

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
                currentTime.add(Calendar.MINUTE, -2);// COMMON sms send sms with 2 min interval;

                if(CommonCache.LAST_SENT_SMS.getGmtModify().before(currentTime.getTime())){
                    smsQueue.setStatus(Utility.SMS_QUEUE_SENDING);
                    smsQueue.setGmtModify(Utility.getCurrentDate());
                    smsQueueRepo.save(smsQueue);
                    CommonCache.LAST_SENT_SMS = smsQueue;
                    return new QueueSmsResponse(smsQueue.getId().toString(),smsQueue.getReceiverPhone(),smsQueue.getContent());
                }
            }else{
                smsQueue.setStatus(Utility.SMS_QUEUE_SENDING);
                smsQueue.setGmtModify(Utility.getCurrentDate());
                smsQueueRepo.save(smsQueue);
                CommonCache.LAST_SENT_SMS = smsQueue;
                return new QueueSmsResponse(smsQueue.getId().toString(),smsQueue.getReceiverPhone(),smsQueue.getContent());
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
        smsService.prepareSmsData();
        return null;
    }
}
