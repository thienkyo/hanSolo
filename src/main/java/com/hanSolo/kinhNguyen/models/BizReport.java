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

    @Column(name = "year", length = 2)
    private String year;

    @Column(name = "month", length = 2)
    private String month;

    @Column(name = "in")
    private Integer in;

    @Column(name = "out")
    private Integer out;

    @Column(name = "gmt_create", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtCreate;

    @Column(name = "gmt_modify", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtModify;

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

    public Integer getOut() {
        return out;
    }

    public void setOut(Integer out) {
        this.out = out;
    }

    public Integer getIn() {
        return in;
    }

    public void setIn(Integer in) {
        this.in = in;
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