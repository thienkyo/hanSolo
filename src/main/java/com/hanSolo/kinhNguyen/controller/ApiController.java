package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.SmsQueue;
import com.hanSolo.kinhNguyen.repository.SmsQueueRepository;
import com.hanSolo.kinhNguyen.repository.SmsUserInfoRepository;
import com.hanSolo.kinhNguyen.response.QueueSmsResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired private SmsQueueRepository smsQueueRepo;
    @Autowired private SmsUserInfoRepository smsUserInfoRepo;

    @RequestMapping("getQueueSms")
    public QueueSmsResponse getQueueSms() {
        Optional<SmsQueue> smsQueueOpt = smsQueueRepo.findFirstByStatusOrderByGmtCreateAsc("INIT");



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
}
