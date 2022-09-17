package com.hanSolo.kinhNguyen.response;

public class GenericResponse extends BaseResponse{
    private String replyStr;

    public GenericResponse(String replyStr, String errorCode, String errorMessage) {
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
