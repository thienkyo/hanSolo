package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.*;
import com.hanSolo.kinhNguyen.repository.*;
import com.hanSolo.kinhNguyen.response.GenericResponse;
import com.hanSolo.kinhNguyen.response.LoginResponse;
import com.hanSolo.kinhNguyen.response.MemberResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/authenticated")
public class AuthenticationController {

    @Autowired private MemberRepository memberRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private CouponRepository couponRepo;
    @Autowired private SmsUserInfoRepository smsUserInfoRepo;
    @Autowired private UsedCouponsRepository usedCouponsRepo;
    @Autowired private SpecificSmsUserInfoRepository specificSmsUserInfoRepo;

    @RequestMapping(value = "me", method = RequestMethod.GET)
    public MemberResponse getMe(final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");
        Optional<Member> memOpt = memberRepo.findByPhoneAndStatus(claims.get("sub")+"", Utility.ACTIVE_STATUS);
        if (memOpt.isEmpty() ) {
            return new MemberResponse(null, Utility.FAIL_ERRORCODE,"member not exist or disable");
        }
        memOpt.get().setPass("");
        return new MemberResponse(memOpt.get(), Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "saveOrder", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse saveOrder(@RequestBody final Order order, final HttpServletRequest request) throws ServletException, ParseException {
        final Claims claims = (Claims) request.getAttribute("claims");
        Optional<Member> memOpt = memberRepo.findByPhoneAndStatus(claims.get("sub")+"", Utility.ACTIVE_STATUS);
        if (memOpt.isEmpty() ) {
            return new GenericResponse(null, Utility.FAIL_ERRORCODE,"member not exist or disable");
        }
        order.setMember(memOpt.get());
        Order or = orderRepo.save(order);

        updateCouponQuantity(order,order.getCurrentCouponCode(),order.getCouponCode());

        List<SmsUserInfo> smsUserList = new ArrayList<>();
        smsUserList.add(new SmsUserInfo(order.getShippingName(), order.getShippingPhone(), order.getGender(),order.getGmtCreate(),
                order.getGmtCreate(),Utility.getCurrentDate(),Utility.getCurrentDate(), order.getLocation(),
                order.getShippingAddress(),order.getAreaCode()));
        for(OrderDetail item : order.getOrderDetails()){
            if(!item.getPhone().isEmpty() && !item.getPhone().equals(order.getShippingPhone()) && order.getLocation().equals("STORE")){
                smsUserList.add(new SmsUserInfo(item.getName(), item.getPhone(), item.getGender(),item.getGmtCreate(),item.getGmtCreate(),
                        Utility.getCurrentDate(),Utility.getCurrentDate(), order.getLocation(),item.getAddress(), order.getAreaCode()));
            }

            updateCouponQuantity(order,item.getCurrentFrameDiscountCode(),item.getFrameDiscountCode());
            updateCouponQuantity(order,item.getCurrentLensDiscountCode(),item.getLensDiscountCode());
        }
        List<SmsUserInfo> smsUserResult = new ArrayList<>();
        for(SmsUserInfo smsUserInfo : smsUserList){
            if(smsUserInfo.getPhone().replace(" ","").length() < 10){
                continue;
            }
            Optional<SmsUserInfo> userInfoDBOtp = smsUserInfoRepo.findByPhone(smsUserInfo.getPhone());
            if(userInfoDBOtp.isPresent()){
                String name = smsUserInfo.getName();
                boolean gender = smsUserInfo.getGender();
                smsUserInfo = userInfoDBOtp.get();
                // new order but with same patient.
                if(0 ==  order.getId()){
                    smsUserInfo.setJobIdList("");
                    smsUserInfo.setOrderCreateDate(order.getGmtCreate());
                    smsUserInfo.setLastSendSmsDate(order.getGmtCreate());
                }
                smsUserInfo.setName(name);
                smsUserInfo.setGender(gender);
            }
            smsUserInfo.setAreaCode(order.getAreaCode());
            smsUserInfo.setAddress(order.getShippingAddress());
            smsUserInfo.setGmtModify(Utility.getCurrentDate());
            smsUserResult.add(smsUserInfo);
        }
        smsUserInfoRepo.saveAll(smsUserResult);

        Optional<SpecificSmsUserInfo> specSmsDBOtp = specificSmsUserInfoRepo.findByPhone(order.getShippingPhone());
        if(order.getSpecificJobId() != 0 ){
            SpecificSmsUserInfo specSms;
            if(specSmsDBOtp.isEmpty()){
                specSms = new SpecificSmsUserInfo();
                specSms.setGender(order.getGender());
                specSms.setLastSendSmsDate(order.getGmtCreate());
                specSms.setOrderCreateDate(order.getGmtCreate());
                specSms.setGmtCreate(Utility.getCurrentDate());
                specSms.setPhone(order.getShippingPhone());
                specSms.setName(order.getShippingName());
                specSms.setJobIdList("");
                specSms.setLocation("STORE");
            }else{
                specSms = specSmsDBOtp.get();
                if( 0 ==  order.getId()){
                    specSms.setJobIdList("");
                    specSms.setOrderCreateDate(order.getGmtCreate());
                }
            }
            specSms.setAddress(order.getShippingAddress());
            specSms.setGender(order.getGender());
            specSms.setJobIdToRun(order.getSpecificJobId().toString());
            specSms.setGmtModify(Utility.getCurrentDate());
            specificSmsUserInfoRepo.save(specSms);
        }else{
            if(specSmsDBOtp.isPresent()){
                specificSmsUserInfoRepo.delete(specSmsDBOtp.get());
            }
        }

        /// coupon
        if(StringUtils.hasText(or.getCouponCode())){
            int orderAmount = 0;
            for( OrderDetail orderDetail : or.getOrderDetails()){
                orderAmount += orderDetail.getFramePriceAtThatTime() + orderDetail.getLensPrice();
            }
            usedCouponsRepo.save(new UsedCoupons(or.getId(),or.getCouponCode(),or.getCouponDiscount(),orderAmount,or.getGmtCreate(),or.getShippingName()));
        }

        GenericResponse response = or == null ? new GenericResponse("",Utility.FAIL_ERRORCODE,"save order fail") : new GenericResponse(or.getId()+"",Utility.SUCCESS_ERRORCODE,"save order success");
        return response;
    }

    @RequestMapping(value = "updateMe", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse updateMe(@RequestBody final Member member) throws ServletException {
        Optional<Member> memOpt = memberRepo.findByPhoneAndStatus(member.getPhone(), Utility.ACTIVE_STATUS);
        if (memOpt.isEmpty()) {
            return new GenericResponse("",Utility.FAIL_ERRORCODE,"member not exist or disable");
        }
        Member m = memOpt.get();
        if(member.getNewPass() != null && !member.getNewPass().isEmpty()){
            if(member.getOldPass() != null && !member.getOldPass().equals(memOpt.get().getPass())){
                return new GenericResponse("",Utility.FAIL_ERRORCODE,"wrong pass");
            }else{
                m.setPass(member.getNewPass());
            }
        }
        m.setAddress(member.getAddress());
        m.setFullName(member.getFullName());
        m.setEmail(m.getEmail());
        m.setGmtModify(new Date());
        memberRepo.save(m);
        return new GenericResponse(m.getId()+"",Utility.SUCCESS_ERRORCODE,"save member success");
    }

    private void updateCouponQuantity(Order order,String currentCode, String newCode){
        if(!StringUtils.hasText(newCode)){
            if(StringUtils.hasText(currentCode)){
                Optional<Coupon>  currentCouponOpt = couponRepo.findByCode(currentCode);
                Coupon currentCoupon = currentCouponOpt.get();
                currentCoupon.setQuantity(currentCoupon.getQuantity()+1);
                couponRepo.save(currentCoupon);
            }
            return;
        }
        Optional<Coupon>  newCouponOpt = couponRepo.findByCode(newCode);
        if(newCouponOpt.isPresent()){
            Coupon coupon = newCouponOpt.get();
            // new order
            if(order.getId() == 0){
                coupon.setQuantity(coupon.getQuantity()-1);
                couponRepo.save(coupon);
            }else if( StringUtils.hasText(newCode) && !newCode.equals(currentCode)){
                // update existing order with new code and subtract amount of new code
                coupon.setQuantity(coupon.getQuantity()-1);
                couponRepo.save(coupon);
                if(StringUtils.hasText(currentCode)){
                    Optional<Coupon>  currentCouponOpt = couponRepo.findByCode(currentCode);
                    Coupon currentCoupon = currentCouponOpt.get();
                    currentCoupon.setQuantity(currentCoupon.getQuantity()+1);
                    couponRepo.save(currentCoupon);
                }
            }
        }
    }

}
