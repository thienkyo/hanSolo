package com.hanSolo.kinhNguyen.models;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ParentCodeModel {
    @Column(name = "client_id", nullable = false)
    private Integer clientId;

    @Column(name = "shop_id", nullable = false, length = 20)
    private Integer shopId;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
}