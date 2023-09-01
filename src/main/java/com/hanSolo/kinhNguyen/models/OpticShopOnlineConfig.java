package com.hanSolo.kinhNguyen.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "optic_shop_online_config")
public class OpticShopOnlineConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "owner_phone", length = 20)
    private String ownerPhone;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "biz_report_begin_month_number")
    private Integer bizReportBeginMonthNumber;

    @Column(name = "biz_report_end_month_number")
    private Integer bizReportEndMonthNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}