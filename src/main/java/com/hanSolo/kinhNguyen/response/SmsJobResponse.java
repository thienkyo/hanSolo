package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.SmsJob;

public class SmsJobResponse extends BaseResponse{
    private SmsJob smsJob;

    public SmsJobResponse(SmsJob smsJob,String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        this.smsJob = smsJob;
    }

    public SmsJob getSmsJob() {
        return smsJob;
    }

    public void setSmsJob(SmsJob smsJob) {
        this.smsJob = smsJob;
    }
}
