package com.hanSolo.kinhNguyen.models;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ParentCodeModel {
    @Column(name = "client_code", nullable = false)
    private String clientCode;

    @Column(name = "shop_code", length = 20)
    private String shopCode;

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }
}