package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Coupon;
import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.repository.CouponRepository;
import com.hanSolo.kinhNguyen.response.GenericResponse;
import com.hanSolo.kinhNguyen.response.LoginResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/coupon")
public class CouponController {
    @Autowired
    private CouponRepository couponRepo;

    @RequestMapping("getByCode/{code}/")
    public GenericResponse getCouponByCode(@PathVariable String code) {

        Optional<Coupon> couponOpt = couponRepo.findByCode(code);

        if ( couponOpt.isEmpty() ) {
            return new GenericResponse("Không tồn tại",Utility.FAIL_ERRORCODE,"Không tồn tại.");
        }

        Coupon coupon = couponOpt.get();

        if(coupon.getQuantity() <= 0){
            return new GenericResponse("Số lượng đã hết",Utility.FAIL_ERRORCODE,"Số lượng đã hết.");
        }

        Date expiredDate = new Date(coupon.getGmtModify().getTime() + (1000 * 60 * 60 * 24 * coupon.getLifespan().longValue()));
        Date today = new Date();

        if(today.after(expiredDate)){
            return new GenericResponse("Hết hạn",Utility.FAIL_ERRORCODE,"Hết hạn.");
        }

        return new GenericResponse(coupon.getValue()+"",Utility.SUCCESS_ERRORCODE,"success.");
    }

    @RequestMapping("getByCode2/{code}/{type}")
    public GenericResponse getCouponByCode2(@PathVariable String code,@PathVariable String type) {

        Optional<Coupon> couponOpt = couponRepo.findByCodeAndCouponType(code,type);

        if ( couponOpt.isEmpty() ) {
            return new GenericResponse("Không tồn tại",Utility.FAIL_ERRORCODE,"Không tồn tại.");
        }

        Coupon coupon = couponOpt.get();

        if(coupon.getQuantity() <= 0){
            return new GenericResponse("Số lượng đã hết",Utility.FAIL_ERRORCODE,"Số lượng đã hết.");
        }

        Date expiredDate = new Date(coupon.getGmtModify().getTime() + (1000 * 60 * 60 * 24 * coupon.getLifespan().longValue()));
        Date today = new Date();

        if(today.after(expiredDate)){
            return new GenericResponse("Hết hạn",Utility.FAIL_ERRORCODE,"Hết hạn.");
        }

        return new GenericResponse(coupon.getValue()+"",Utility.SUCCESS_ERRORCODE,"success.");
    }
}
