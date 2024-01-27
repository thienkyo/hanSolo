package com.hanSolo.kinhNguyen.DTO;

public class SmsStringResult {

    String smsString;
    String extendInfo;

    public SmsStringResult(String smsString, String extendInfo) {
        this.smsString = smsString;
        this.extendInfo = extendInfo;
    }

    public String getSmsString() {
        return smsString;
    }

    public void setSmsString(String smsString) {
        this.smsString = smsString;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }
}
