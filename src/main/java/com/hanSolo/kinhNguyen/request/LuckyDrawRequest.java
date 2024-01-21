package com.hanSolo.kinhNguyen.request;

public class LuckyDrawRequest {
    private String orderIdList;
    private String clientCode;
    private String shopCode;
    private int expiry;
    private int smsJobId;
    private int couponValue;

    public int getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(int couponValue) {
        this.couponValue = couponValue;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(String orderIdList) {
        this.orderIdList = orderIdList;
    }

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public int getSmsJobId() {
        return smsJobId;
    }

    public void setSmsJobId(int smsJobId) {
        this.smsJobId = smsJobId;
    }
}
