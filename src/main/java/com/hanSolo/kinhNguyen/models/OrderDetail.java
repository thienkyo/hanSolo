package com.hanSolo.kinhNguyen.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "frame_discount_at_that_time")
    private Integer frameDiscountAtThatTime;

    @Column(name = "frame_price_at_that_time", nullable = false)
    private Integer framePriceAtThatTime;

    @Column(name = "os_vasc", length = 10)
    private String osVasc;

    @Column(name = "os_vacc", length = 10)
    private String osVacc;

    @Column(name = "os_sphere", length = 10)
    private String osSphere;

    @Column(name = "os_cylinder", length = 10)
    private String osCylinder;

    @Column(name = "os_axis", length = 10)
    private String osAxis;

    @Column(name = "os_prism", length = 10)
    private String osPrism;

    @Column(name = "od_vasc", length = 10)
    private String odVasc;

    @Column(name = "od_vacc", length = 10)
    private String odVacc;

    @Column(name = "od_sphere", length = 10)
    private String odSphere;

    @Column(name = "od_cylinder", length = 10)
    private String odCylinder;

    @Column(name = "od_axis", length = 10)
    private String odAxis;

    @Column(name = "od_prism", length = 10)
    private String odPrism;

    @Column(name = "os_add", length = 10)
    private String osAdd;

    @Column(name = "od_add", length = 10)
    private String odAdd;

    @Column(name = "pd", length = 10)
    private String pd;

    @Column(name = "wd", length = 10)
    private String wd;

    @Column(name = "va_near", length = 10)
    private String vaNear;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "relationship", length = 200)
    private String relationship;

    @Column(name = "recommended_spectacles", length = 300)
    private String recommendedSpectacles;

    @Column(name = "gmt_create", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtCreate;

    @Column(name = "gmt_modify", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtModify;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public String getRecommendedSpectacles() {
        return recommendedSpectacles;
    }

    public void setRecommendedSpectacles(String recommendedSpectacles) {
        this.recommendedSpectacles = recommendedSpectacles;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVaNear() {
        return vaNear;
    }

    public void setVaNear(String vaNear) {
        this.vaNear = vaNear;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getOdAdd() {
        return odAdd;
    }

    public void setOdAdd(String odAdd) {
        this.odAdd = odAdd;
    }

    public String getOsAdd() {
        return osAdd;
    }

    public void setOsAdd(String osAdd) {
        this.osAdd = osAdd;
    }

    public String getOdPrism() {
        return odPrism;
    }

    public void setOdPrism(String odPrism) {
        this.odPrism = odPrism;
    }

    public String getOdAxis() {
        return odAxis;
    }

    public void setOdAxis(String odAxis) {
        this.odAxis = odAxis;
    }

    public String getOdCylinder() {
        return odCylinder;
    }

    public void setOdCylinder(String odCylinder) {
        this.odCylinder = odCylinder;
    }

    public String getOdSphere() {
        return odSphere;
    }

    public void setOdSphere(String od_sphere) {
        this.odSphere = odSphere;
    }

    public String getOdVacc() {
        return odVacc;
    }

    public void setOdVacc(String odVacc) {
        this.odVacc = odVacc;
    }

    public String getOdVasc() {
        return odVasc;
    }

    public void setOdVasc(String odVasc) {
        this.odVasc = odVasc;
    }

    public String getOsPrism() {
        return osPrism;
    }

    public void setOsPrism(String osPrism) {
        this.osPrism = osPrism;
    }

    public String getOsAxis() {
        return osAxis;
    }

    public void setOsAxis(String osAxis) {
        this.osAxis = osAxis;
    }

    public String getOsCylinder() {
        return osCylinder;
    }

    public void setOsCylinder(String osCylinder) {
        this.osCylinder = osCylinder;
    }

    public String getOsSphere() {
        return osSphere;
    }

    public void setOsSphere(String os_sphere) {
        this.osSphere = os_sphere;
    }

    public String getOsVacc() {
        return osVacc;
    }

    public void setOsVacc(String osVacc) {
        this.osVacc = osVacc;
    }

    public String getOsVasc() {
        return osVasc;
    }

    public void setOsVasc(String osVasc) {
        this.osVasc = osVasc;
    }

    public Integer getFramePriceAtThatTime() {
        return framePriceAtThatTime;
    }

    public void setFramePriceAtThatTime(Integer framePriceAtThatTime) {
        this.framePriceAtThatTime = framePriceAtThatTime;
    }

    public Integer getFrameDiscountAtThatTime() {
        return frameDiscountAtThatTime;
    }

    public void setFrameDiscountAtThatTime(Integer frameDiscountAtThatTime) {
        this.frameDiscountAtThatTime = frameDiscountAtThatTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}