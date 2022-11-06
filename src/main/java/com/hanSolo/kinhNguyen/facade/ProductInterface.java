package com.hanSolo.kinhNguyen.facade;

import com.hanSolo.kinhNguyen.models.Category;
import com.hanSolo.kinhNguyen.models.Supplier;

import java.util.Collection;
import java.util.Date;

public interface ProductInterface {
    public String getWeight();
    public String getThumbnail();
    public Collection<Category> getCategories();
    public Date getGmtModify();
    public Date getGmtCreate();
    public Integer getQuantity();
    public String getImages();
    public String getDescription();
    public Boolean getStatus();
    public Integer getDiscount();
    public Integer getSellPrice();
    public String getName();
    public String getMerchantProductId();
    public Integer getId();
}
