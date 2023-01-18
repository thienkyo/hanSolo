package com.hanSolo.kinhNguyen.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "key_management")
public class KeyManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "gmt_modify", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtModify;

    @Column(name = "gmt_create", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtCreate;

    @Column(name = "key_purpose", length = 20)
    private String keyPurpose;

    @Column(name = "secretKey", length = 100)
    private String secretKey;

    // day
    @Column(name = "timeout")
    private Integer timeout;

    @Column(name = "token", length = 600)
    private String token;

    @Column(name = "roles")
    private String roles;

    @Column(name = "name")
    private String name;

    @Column(name = "is_activate")
    private Boolean isActivate;

    public Boolean getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(Boolean isActivate) {
        this.isActivate = isActivate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getKeyPurpose() {
        return keyPurpose;
    }

    public void setKeyPurpose(String keyPurpose) {
        this.keyPurpose = keyPurpose;
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