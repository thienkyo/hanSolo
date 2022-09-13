package com.hanSolo.kinhNguyen.response;

public class LoginResponse extends BaseResponse{

    private String token;

    public LoginResponse(String token, String errorCode, String errorMessage) {
        super(errorCode,errorMessage);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
