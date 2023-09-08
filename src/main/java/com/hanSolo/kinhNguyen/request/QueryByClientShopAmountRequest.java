package com.hanSolo.kinhNguyen.request;

/**
 * for query with clientCode, shopCode, Amount for all cases.
 */
public class QueryByClientShopAmountRequest {

    String clientCode;

    String shopCode;

    int amount;

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
