package com.hanSolo.kinhNguyen.request;

/**
 * for query with clientCode, shopCode, Amount for all cases.
 */
public class QueryByClientShopAmountRequest {

    /**
     * code of client
     */
    String clientCode;

    /**
     * code of shop
     */
    String shopCode;

    /**
     * amount of record: 0 means ALL.
     */
    int amount;

    /**
     * for general purpose
     * ex: in search feature, it is a keySearch string
     */
    String generalPurpose;

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

    public String getGeneralPurpose() {
        return generalPurpose;
    }

    public void setGeneralPurpose(String generalPurpose) {
        this.generalPurpose = generalPurpose;
    }
}
