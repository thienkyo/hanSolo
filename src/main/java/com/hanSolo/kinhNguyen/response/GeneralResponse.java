package com.hanSolo.kinhNguyen.response;

public class GeneralResponse<T> extends BaseResponse{

    private T obj;

    public GeneralResponse(T obj, String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        this.obj = obj;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
