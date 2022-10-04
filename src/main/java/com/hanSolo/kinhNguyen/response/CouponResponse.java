package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.Coupon;

public class CouponResponse extends BaseResponse{

    private Coupon coupon;

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public CouponResponse(Coupon coupon,String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        this.coupon = coupon;
    }
}
