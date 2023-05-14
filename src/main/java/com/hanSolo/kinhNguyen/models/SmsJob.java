package com.hanSolo.kinhNguyen.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sms_job")
public class SmsJob {
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

    @Column(name = "job_name", length = 100)
    private String jobName;

    @Column(name = "intervalTime")
    private Integer intervalTime;

    @Column(name = "msg_content_template", length = 450)
    private String msgContentTemplate;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "specific_phones", length = 880)
    private String specificPhones;

    @Column(name = "no_sms_days")
    private Integer noSmsDays;

    @Column(name = "is_test")
    private Boolean isTest;

    @Column(name = "job_type", length = 30)
    private String jobType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "weight", length = 2, columnDefinition = "varchar(2) default '1'")
    private String weight;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Boolean getIsTest() {
        return isTest;
    }

    public void setIsTest(Boolean isTest) {
        this.isTest = isTest;
    }

    public Integer getNoSmsDays() {
        return noSmsDays;
    }

    public void setNoSmsDays(Integer noSmsDays) {
        this.noSmsDays = noSmsDays;
    }

    public String getSpecificPhones() {
        return specificPhones;
    }

    public void setSpecificPhones(String specificPhones) {
        this.specificPhones = specificPhones;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsgContentTemplate() {
        return msgContentTemplate;
    }

    public void setMsgContentTemplate(String msgContentTemplate) {
        this.msgContentTemplate = msgContentTemplate;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getGmtModify() {return gmtModify;}

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {this.gmtCreate = gmtCreate;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}