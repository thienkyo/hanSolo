package com.hanSolo.kinhNguyen.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customer_source_report")
public class CustomerSourceReport {

    public CustomerSourceReport() {
    }

    public CustomerSourceReport(Date gmtCreate, Date gmtModify, String year, String month, Integer customerSourceId,Integer count) {
        this.gmtCreate = gmtCreate;
        this.gmtModify = gmtModify;
        this.year = year;
        this.month = month;
        this.customerSourceId = customerSourceId;
        this.count = count;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "gmt_create", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtCreate;

    @Column(name = "gmt_modify", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtModify;

    @Column(name = "year", length = 4)
    private String year;

    @Column(name = "month", length = 2)
    private String month;

    @Column(name = "count")
    private Integer count;

    @Column(name = "customer_source_id")
    private Integer customerSourceId;

    public Integer getCustomerSourceId() {
        return customerSourceId;
    }

    public void setCustomerSourceId(Integer customerSourceId) {
        this.customerSourceId = customerSourceId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}