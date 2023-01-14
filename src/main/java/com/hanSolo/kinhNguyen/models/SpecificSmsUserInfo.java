package com.hanSolo.kinhNguyen.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "specific_sms_user_info")
public class SpecificSmsUserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "phone", nullable = false, unique = true, length = 40)
    private String phone;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "last_send_sms_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSendSmsDate;

    @Column(name = "gmt_create", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtCreate;

    @Column(name = "gmt_modify", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtModify;

    @Column(name = "order_create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderCreateDate;

    /**
     * list of job id that already run
     */
    @Column(name = "job_id_list", length = 200)
    private String jobIdList;

    @Column(name = "name")
    private String name;

    @Column(name = "is_test_user")
    private Boolean isTestUser;

    @Column(name = "location", length = 50)
    private String location;

    @Column(name = "address", length = 400)
    private String address;

    @Column(name = "job_id_to_run")
    private String jobIdToRun;

    public String getJobIdToRun() {
        return jobIdToRun;
    }

    public void setJobIdToRun(String jobIdToRun) {
        this.jobIdToRun = jobIdToRun;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getIsTestUser() {
        return isTestUser;
    }

    public void setIsTestUser(Boolean isTestUser) {
        this.isTestUser = isTestUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpecificSmsUserInfo() {
    }

    public SpecificSmsUserInfo(String name,String phone, Boolean gender, Date lastSendSmsDate, Date orderCreateDate,Date gmtCreate, Date gmtModify,String location, String address) {
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.location = location;
        this.lastSendSmsDate = lastSendSmsDate;
        this.orderCreateDate = orderCreateDate;
        this.address = address;
        this.gmtCreate = gmtCreate;
        this.gmtModify = gmtModify;
        this.jobIdList = "";
    }


    public String getJobIdList() {
        return jobIdList;
    }

    public void setJobIdList(String jobIdList) {
        this.jobIdList = jobIdList;
    }

    public Date getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(Date orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
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

    public Date getLastSendSmsDate() {
        return lastSendSmsDate;
    }

    public void setLastSendSmsDate(Date lastSendSmsDate) {
        this.lastSendSmsDate = lastSendSmsDate;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}