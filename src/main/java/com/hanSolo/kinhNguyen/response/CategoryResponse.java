package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.Category;

public class CategoryResponse extends BaseResponse{

    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CategoryResponse(Category category, String errorCode, String errorMessage) {
        super(errorCode,errorMessage);
        this.category = category;
    }
}
