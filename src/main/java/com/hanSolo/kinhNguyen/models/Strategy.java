package com.hanSolo.kinhNguyen.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "strategy")
public class Strategy extends BaseModel {
    @Column(name = "name", length = 120)
    private String name;

    @Column(name = "template_content")
    private String templateContent;

    @Column(name = "location", length = 50)
    private String location;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "discount_value")
    private Integer discountValue;

    @Column(name = "is_coupon_created", columnDefinition = "boolean default false")
    private Boolean isCouponCreated;

    @Column(name = "is_sms_created", columnDefinition = "boolean default false")
    private Boolean isSmsCreated;

    public Boolean getIsSmsCreated() {
        return isSmsCreated;
    }

    public void setIsSmsCreated(Boolean isSmsCreated) {
        this.isSmsCreated = isSmsCreated;
    }

    public Boolean getIsCouponCreated() {
        return isCouponCreated;
    }

    public void setIsCouponCreated(Boolean isCouponCreated) {
        this.isCouponCreated = isCouponCreated;
    }

    public Integer getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Integer discountValue) {
        this.discountValue = discountValue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}