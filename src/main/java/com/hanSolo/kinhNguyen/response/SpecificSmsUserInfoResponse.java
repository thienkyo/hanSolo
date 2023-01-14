package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.SpecificSmsUserInfo;

public class SpecificSmsUserInfoResponse extends BaseResponse{

    private SpecificSmsUserInfo specificSmsUserInfo;

    public SpecificSmsUserInfo getSpecificSmsUserInfo() {
        return specificSmsUserInfo;
    }

    public void setSpecificSmsUserInfo(SpecificSmsUserInfo specificSmsUserInfo) {
        this.specificSmsUserInfo = specificSmsUserInfo;
    }

    public SpecificSmsUserInfoResponse(SpecificSmsUserInfo specificSmsUserInfo, String errorCode, String errorMessage) {
        super(errorCode,errorMessage);
        this.specificSmsUserInfo = specificSmsUserInfo;
    }
}
