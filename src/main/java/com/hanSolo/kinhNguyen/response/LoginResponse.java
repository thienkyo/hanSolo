package com.hanSolo.kinhNguyen.response;

public class LoginResponse extends BaseResponse{

    private String token;

    public LoginResponse(String token, String errorCode, String errorMessage) {
        this.token = token;
        super.setErrorCode(errorCode);
        super.setErrorMessage(errorMessage);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
