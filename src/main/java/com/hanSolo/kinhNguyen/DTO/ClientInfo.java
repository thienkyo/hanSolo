package com.hanSolo.kinhNguyen.DTO;

public class ClientInfo {
    private String brandName;
    private String address;
    private String phone;
    private String clientCode;
    private Boolean unlockSmsFeature;

    public Boolean getUnlockSmsFeature() {
        return unlockSmsFeature;
    }

    public void setUnlockSmsFeature(Boolean unlockSmsFeature) {
        this.unlockSmsFeature = unlockSmsFeature;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
