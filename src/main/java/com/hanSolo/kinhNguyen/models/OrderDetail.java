package com.hanSolo.kinhNguyen.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_detail")
public class OrderDetail extends ParentCodeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "frame_discount_at_that_time")
    private Integer frameDiscountAtThatTime=0;

    @Column(name = "frame_price_at_that_time", nullable = false)
    private Integer framePriceAtThatTime=0;

    @Column(name = "frame_note", length = 700)
    private String frameNote;

    @Column(name = "os_vasc", length = 50)
    private String osVasc;

    @Column(name = "os_vacc", length = 50)
    private String osVacc;

    @Column(name = "os_sphere", length = 50)
    private String osSphere;

    @Column(name = "os_cylinder", length = 50)
    private String osCylinder;

    @Column(name = "os_axis", length = 50)
    private String osAxis;

    @Column(name = "os_prism", length = 50)
    private String osPrism;

    @Column(name = "od_vasc", length = 50)
    private String odVasc;

    @Column(name = "od_vacc", length = 50)
    private String odVacc;

    @Column(name = "od_sphere", length = 50)
    private String odSphere;

    @Column(name = "od_cylinder", length = 50)
    private String odCylinder;

    @Column(name = "od_axis", length = 20)
    private String odAxis;

    @Column(name = "od_prism", length = 20)
    private String odPrism;

    @Column(name = "os_add", length = 20)
    private String osAdd;

    @Column(name = "od_add", length = 20)
    private String odAdd;

    @Column(name = "pd", length = 20)
    private String pd;

    @Column(name = "wd", length = 20)
    private String wd;

    @Column(name = "va_near", length = 20)
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

    @Column(name = "yob")
    private String yob;

    @Column(name = "lens_price")
    private Integer lensPrice = 0;

    @Column(name = "lens_note", length = 700)
    private String lensNote;

    @Column(name = "address", length = 700)
    private String address;

    @Column(name = "order_detail_note", length = 800)
    private String orderDetailNote;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "od_previous_prescript", length = 40)
    private String odPreviousPrescript;

    @Column(name = "os_previous_prescript", length = 40)
    private String osPreviousPrescript;

    @Column(name = "order_detail_note_for_cus", length = 600)
    private String orderDetailNoteForCus;

    @Column(name = "reading")
    private Boolean reading;

    @Column(name = "mono_lens")
    private Boolean monoLens;

    @Column(name = "other_price", columnDefinition = "integer default 0")
    private Integer otherPrice;

    @Column(name = "other_note", length = 600)
    private String otherNote;

    @Column(name = "frame_discount_code", length = 20)
    private String frameDiscountCode;

    @Column(name = "frame_discount_amount", columnDefinition = "integer default 0")
    private Integer frameDiscountAmount;

    @Column(name = "lens_discount_code", length = 20)
    private String lensDiscountCode;

    @Column(name = "lens_discount_amount", columnDefinition = "integer default 0")
    private Integer lensDiscountAmount;


    @Transient
    private String currentLensDiscountCode;

    @Transient
    private String currentFrameDiscountCode;

    @Column(name = "lens_quantity", columnDefinition = "integer default 1")
    private Integer lensQuantity;


    public OrderDetail( ) {}

    public OrderDetail( String name, String phone, String address, Date gmtCreate) {
        this.name = name;
        this.phone = phone;
        this.gmtCreate = gmtCreate;
        this.address = address;
    }


    public Integer getLensQuantity() {
        return lensQuantity;
    }

    public void setLensQuantity(Integer lensQuantity) {
        this.lensQuantity = lensQuantity;
    }

    public String getCurrentFrameDiscountCode() {
        return currentFrameDiscountCode;
    }

    public void setCurrentFrameDiscountCode(String currentFrameDiscountCode) {
        this.currentFrameDiscountCode = currentFrameDiscountCode;
    }

    public String getCurrentLensDiscountCode() {
        return currentLensDiscountCode;
    }

    public void setCurrentLensDiscountCode(String currentLensDiscountCode) {
        this.currentLensDiscountCode = currentLensDiscountCode;
    }

    public Integer getLensDiscountAmount() {
        return lensDiscountAmount;
    }

    public void setLensDiscountAmount(Integer lensDiscountAmount) {
        this.lensDiscountAmount = lensDiscountAmount;
    }

    public String getLensDiscountCode() {
        return lensDiscountCode;
    }

    public void setLensDiscountCode(String lensDiscountCode) {
        this.lensDiscountCode = lensDiscountCode;
    }

    public Integer getFrameDiscountAmount() {
        return frameDiscountAmount;
    }

    public void setFrameDiscountAmount(Integer frameDiscountAmount) {
        this.frameDiscountAmount = frameDiscountAmount;
    }

    public String getFrameDiscountCode() {
        return frameDiscountCode;
    }

    public void setFrameDiscountCode(String frameDiscountCode) {
        this.frameDiscountCode = frameDiscountCode;
    }

    public String getOtherNote() {
        return otherNote;
    }

    public void setOtherNote(String otherNote) {
        this.otherNote = otherNote;
    }

    public Integer getOtherPrice() {
        return otherPrice;
    }

    public void setOtherPrice(Integer otherPrice) {
        this.otherPrice = otherPrice;
    }

    public Boolean getMonoLens() {
        return monoLens;
    }

    public void setMonoLens(Boolean monoLens) {
        this.monoLens = monoLens;
    }

    public Boolean getReading() {
        return reading;
    }

    public void setReading(Boolean reading) {
        this.reading = reading;
    }

    public String getOrderDetailNoteForCus() {
        return orderDetailNoteForCus;
    }

    public void setOrderDetailNoteForCus(String orderDetailNoteForCus) {
        this.orderDetailNoteForCus = orderDetailNoteForCus;
    }

    public String getOsPreviousPrescript() {
        return osPreviousPrescript;
    }

    public void setOsPreviousPrescript(String osPreviousPrescript) {
        this.osPreviousPrescript = osPreviousPrescript;
    }

    public String getOdPreviousPrescript() {
        return odPreviousPrescript;
    }

    public void setOdPreviousPrescript(String odPreviousPrescript) {
        this.odPreviousPrescript = odPreviousPrescript;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderDetailNote() {
        return orderDetailNote;
    }

    public void setOrderDetailNote(String orderDetailNote) {
        this.orderDetailNote = orderDetailNote;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLensNote() {
        return lensNote;
    }

    public void setLensNote(String lensNote) {
        this.lensNote = lensNote;
    }

    public Integer getLensPrice() {
        return lensPrice;
    }

    public void setLensPrice(Integer lensPrice) {
        this.lensPrice = lensPrice;
    }

    public String getFrameNote() {
        return frameNote;
    }

    public void setFrameNote(String frameNote) {
        this.frameNote = frameNote;
    }

    public String getYob() {
        return yob;
    }

    public void setYob(String yob) {
        this.yob = yob;
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

    public void setOdSphere(String odSphere) {
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