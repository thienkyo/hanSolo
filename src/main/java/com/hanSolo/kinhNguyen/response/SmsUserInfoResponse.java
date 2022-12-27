package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.SmsUserInfo;

public class SmsUserInfoResponse extends BaseResponse{

    private SmsUserInfo smsUserInfo;

    public SmsUserInfo getSmsUserInfo() {
        return smsUserInfo;
    }

    public void setSmsUserInfo(SmsUserInfo smsUserInfo) {
        this.smsUserInfo = smsUserInfo;
    }

    public SmsUserInfoResponse(SmsUserInfo smsUserInfo, String errorCode, String errorMessage) {
        super(errorCode,errorMessage);
        this.smsUserInfo = smsUserInfo;
    }
}
