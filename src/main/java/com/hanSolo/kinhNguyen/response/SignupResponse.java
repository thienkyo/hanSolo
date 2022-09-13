package com.hanSolo.kinhNguyen.response;

public class SignupResponse extends BaseResponse{
    private String replyStr;

    public SignupResponse(String replyStr, String errorCode, String errorMessage) {
        super(errorCode,errorMessage);
        this.replyStr = replyStr;
    }

    public String getReplyStr() {
        return replyStr;
    }

    public void setReplyStr(String replyStr) {
        this.replyStr = replyStr;
    }
}
