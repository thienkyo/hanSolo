package com.hanSolo.kinhNguyen.DTO;

import com.hanSolo.kinhNguyen.models.Client;
import com.hanSolo.kinhNguyen.models.Shop;

import java.util.List;

public class ClientShopList {
    private List<Client> clientList;
    private List<Shop> shopList;

    public List<Shop> getOneClientShopList() {
        return oneClientShopList;
    }

    public void setOneClientShopList(List<Shop> oneClientShopList) {
        this.oneClientShopList = oneClientShopList;
    }

    private List<Shop> oneClientShopList;


    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
