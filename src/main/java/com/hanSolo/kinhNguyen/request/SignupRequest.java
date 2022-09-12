package com.hanSolo.kinhNguyen.request;

public class SignupRequest {
    private String signupStr;
    private String fullName;
    private String email;

    public String getSignupStr() {
        return signupStr;
    }

    public void setSignupStr(String signupStr) {
        this.signupStr = signupStr;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
