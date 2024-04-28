package com.hanSolo.kinhNguyen.controller.mgnt;

import com.hanSolo.kinhNguyen.models.Coupon;
import com.hanSolo.kinhNguyen.repository.CouponRepository;
import com.hanSolo.kinhNguyen.request.QueryByClientShopAmountRequest;
import com.hanSolo.kinhNguyen.response.GeneralResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/mgnt/coupon")
public class CouponController {
    @Autowired
    private CouponRepository couponRepo;

    @RequestMapping("getByCode3")
    public GeneralResponse<Coupon> getCouponByCode3(@RequestBody final QueryByClientShopAmountRequest req, final HttpServletRequest request) {
        String[] parts = req.getGeneralPurpose().split("\\|");
        List<Coupon> couponList = couponRepo.findByCodeAndCouponTypeAndQuantityGreaterThanAndClientCodeOrderByGmtCreateDesc(
                    parts[0],parts[1],0, req.getClientCode());

        List<Coupon> result = Utility.filterCoupon(couponList);
        if ( result.isEmpty() ) {
            return new GeneralResponse(null,Utility.FAIL_ERRORCODE,"Không có/đã hết/hết hạn.");
        }

        return new GeneralResponse(result.get(0),Utility.SUCCESS_ERRORCODE,"success.");
    }
}
