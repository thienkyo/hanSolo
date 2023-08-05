package com.hanSolo.kinhNguyen.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "shop_config")
public class ShopConfig extends BaseModel {
    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "shop_owner")
    private String shopOwner;

    @Column(name = "shop_phone", length = 20)
    private String shopPhone;

    @Column(name = "shop_owner_phone", length = 20)
    private String shopOwnerPhone;

    @Column(name = "address")
    private String address;

    @Column(name = "logo_1", length = 100)
    private String logo1;

    @Column(name = "logo_2", length = 100)
    private String logo2;

    @Column(name = "is_unlock_sms_feature",columnDefinition = "boolean default false")
    private Boolean isUnlockSmsFeature;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "price", columnDefinition = "integer default 0")
    private Integer price;

    @Column(name = "other_price", columnDefinition = "integer default 0")
    private Integer otherPrice;

    /**
     * the amount of month excluded in the beginning
     */
    @Column(name = "biz_report_begin_month_number", columnDefinition = "integer default 3")
    private Integer bizReportBeginMonthNumber;

    /**
     * the amount of month excluded in the end.
     */
    @Column(name = "biz_report_end_month_number", columnDefinition = "integer default 1")
    private Integer bizReportEndMonthNumber;

    public Integer getBizReportEndMonthNumber() {
        return bizReportEndMonthNumber;
    }

    public void setBizReportEndMonthNumber(Integer bizReportEndMonthNumber) {
        this.bizReportEndMonthNumber = bizReportEndMonthNumber;
    }

    public Integer getBizReportBeginMonthNumber() {
        return bizReportBeginMonthNumber;
    }

    public void setBizReportBeginMonthNumber(Integer bizReportBeginMonthNumber) {
        this.bizReportBeginMonthNumber = bizReportBeginMonthNumber;
    }

    public Integer getOtherPrice() {
        return otherPrice;
    }

    public void setOtherPrice(Integer otherPrice) {
        this.otherPrice = otherPrice;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Boolean getIsUnlockSmsFeature() {
        return isUnlockSmsFeature;
    }

    public void setIsUnlockSmsFeature(Boolean isUnlockSmsFeature) {
        this.isUnlockSmsFeature = isUnlockSmsFeature;
    }

    public String getLogo2() {
        return logo2;
    }

    public void setLogo2(String logo2) {
        this.logo2 = logo2;
    }

    public String getLogo1() {
        return logo1;
    }

    public void setLogo1(String logo1) {
        this.logo1 = logo1;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopOwnerPhone() {
        return shopOwnerPhone;
    }

    public void setShopOwnerPhone(String shopOwnerPhone) {
        this.shopOwnerPhone = shopOwnerPhone;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getShopOwner() {
        return shopOwner;
    }

    public void setShopOwner(String shopOwner) {
        this.shopOwner = shopOwner;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}