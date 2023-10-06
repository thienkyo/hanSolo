package com.hanSolo.kinhNguyen.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lens_product")
public class LensProduct extends ParentCodeModel {
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

    @Column(name = "lens_note", length = 300)
    private String lensNote;

    @Column(name = "lens_detail")
    private String lensDetail;

    @Column(name = "buy_price")
    private Integer buyPrice;

    @Column(name = "ext_info", length = 700)
    private String extInfo;

    @Column(name = "sell_price")
    private Integer sellPrice;

    public LensProduct() {}

    public LensProduct(Date gmtCreate, Date gmtModify, String lensNote, String lensDetail, String extInfo, Integer sellPrice) {
        this.gmtCreate = gmtCreate;
        this.gmtModify = gmtModify;
        this.lensNote = lensNote;
        this.lensDetail = lensDetail;
        this.extInfo = extInfo;
        this.sellPrice = sellPrice;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public Integer getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Integer buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getLensDetail() {
        return lensDetail;
    }

    public void setLensDetail(String lensDetail) {
        this.lensDetail = lensDetail;
    }

    public String getLensNote() {
        return lensNote;
    }

    public void setLensNote(String lensNote) {
        this.lensNote = lensNote;
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