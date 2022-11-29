package com.hanSolo.kinhNguyen.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "used_coupons")
public class UsedCoupons {

    public UsedCoupons() {
    }

    public UsedCoupons(Integer orderId, String code, Integer couponValue, Integer orderAmount, Date orderDate, String name) {
        this.orderId = orderId;
        this.code = code;
        this.couponValue = couponValue;
        this.orderAmount = orderAmount;
        this.orderDate = orderDate;
        this.name = name;
    }

    @Id
    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "coupon_value", length = 20)
    private Integer couponValue;

    @Column(name = "order_amount")
    private Integer orderAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "name", length = 100)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}