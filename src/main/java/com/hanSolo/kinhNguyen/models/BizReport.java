package com.hanSolo.kinhNguyen.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "biz_report")
public class BizReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "year", length = 4)
    private String year;

    @Column(name = "month", length = 2)
    private String month;

    @Column(name = "income")
    private Integer income;

    @Column(name = "outcome")
    private Integer outcome;

    @Column(name = "gmt_create", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtCreate;

    @Column(name = "gmt_modify", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtModify;

    @Column(name = "quantity")
    private Integer orderQuantity;

    @Column(name = "lens_quantity")
    private Integer lensQuantity;

    @Column(name = "frame_quantity")
    private Integer frameQuantity;

    public Integer getFrameQuantity() {
        return frameQuantity;
    }

    public void setFrameQuantity(Integer frameQuantity) {
        this.frameQuantity = frameQuantity;
    }

    public Integer getLensQuantity() {
        return lensQuantity;
    }

    public void setLensQuantity(Integer lensQuantity) {
        this.lensQuantity = lensQuantity;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getOutcome() {
        return outcome;
    }

    public void setOutcome(Integer outcome) {
        this.outcome = outcome;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}