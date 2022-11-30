package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.CustomerSource;

public class CustomerSourceResponse extends BaseResponse{

    private CustomerSource customerSource;

    public CustomerSource getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(CustomerSource customerSource) {
        this.customerSource = customerSource;
    }

    public CustomerSourceResponse(CustomerSource customerSource, String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        this.customerSource = customerSource;
    }
}
