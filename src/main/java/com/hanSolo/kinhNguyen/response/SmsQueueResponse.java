package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.SmsQueue;

public class SmsQueueResponse extends BaseResponse{
    private SmsQueue smsQueue;

    public SmsQueueResponse(SmsQueue smsQueue,String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        this.smsQueue = smsQueue;
    }

    public SmsQueue getSmsQueue() {
        return smsQueue;
    }

    public void setSmsQueue(SmsQueue smsQueue) {
        this.smsQueue = smsQueue;
    }
}
