package com.hanSolo.kinhNguyen.response;

public class LoginResponse {

    private String token;

    private String data;


    public LoginResponse(String token, String data) {
        this.token = token;
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
