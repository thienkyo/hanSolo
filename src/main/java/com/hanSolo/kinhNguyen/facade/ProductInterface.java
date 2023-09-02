package com.hanSolo.kinhNguyen.facade;

import com.hanSolo.kinhNguyen.models.Category;

import java.util.Collection;
import java.util.Date;

public interface ProductInterface {
    String getWeight();
    String getThumbnail();
    Collection<Category> getCategories();
    Date getGmtModify();
    Date getGmtCreate();
    Integer getQuantity();
    String getImages();
    String getDescription();
    Boolean getStatus();
    Integer getDiscount();
    Integer getSellPrice();
    String getName();
    String getMerchantProductId();
    Integer getId();
}
