package com.hanSolo.kinhNguyen.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "program_result")
public class ProgramResult extends CommonBaseModel {
    @Column(name = "winner_phone", length = 20)
    private String winnerPhone;

    @Column(name = "winner_name", length = 100)
    private String winnerName;

    @Column(name = "order_id")
    private Integer orderId;

    @Temporal(TemporalType.DATE)
    @Column(name = "order_create_date")
    private Date orderCreateDate;

    @Column(name = "expiry", columnDefinition = "integer default 0")
    private Integer expiry;

    @Column(name = "status",columnDefinition = "boolean default false")
    private Boolean status;

    @Column(name = "sms_content")
    private String smsContent;

    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "coupon_value")
    private Integer couponValue;

    @Column(name = "job_name", length = 150)
    private String jobName;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(Integer couponValue) {
        this.couponValue = couponValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getExpiry() {
        return expiry;
    }

    public void setExpiry(Integer expiry) {
        this.expiry = expiry;
    }

    public Date getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(Date orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnerPhone() {
        return winnerPhone;
    }

    public void setWinnerPhone(String winnerPhone) {
        this.winnerPhone = winnerPhone;
    }
}