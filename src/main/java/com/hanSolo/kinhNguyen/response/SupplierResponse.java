package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.Category;
import com.hanSolo.kinhNguyen.models.Supplier;

public class SupplierResponse extends BaseResponse{

    private Supplier supplier;

    public SupplierResponse(Supplier supplier, String errorCode, String errorMessage) {
        super(errorCode,errorMessage);
        this.supplier = supplier;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
