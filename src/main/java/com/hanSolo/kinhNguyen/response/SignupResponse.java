package com.hanSolo.kinhNguyen.response;

public class SignupResponse extends BaseResponse{
    private String replyStr;

    public SignupResponse(String replyStr, String errorCode, String errorMessage) {
        this.replyStr = replyStr;
        super.setErrorCode(errorCode);
        super.setErrorMessage(errorMessage);
    }

    public String getReplyStr() {
        return replyStr;
    }

    public void setReplyStr(String replyStr) {
        this.replyStr = replyStr;
    }
}
