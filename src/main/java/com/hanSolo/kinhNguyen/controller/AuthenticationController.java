package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.*;
import com.hanSolo.kinhNguyen.repository.CouponRepository;
import com.hanSolo.kinhNguyen.repository.MemberRepository;
import com.hanSolo.kinhNguyen.repository.OrderRepository;
import com.hanSolo.kinhNguyen.repository.SmsUserInfoRepository;
import com.hanSolo.kinhNguyen.response.GenericResponse;
import com.hanSolo.kinhNguyen.response.LoginResponse;
import com.hanSolo.kinhNguyen.response.MemberResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

        if(!or.getCouponCode().isBlank()){
            Optional<Coupon>  couponOpt = couponRepo.findByCode(or.getCouponCode());
            if(couponOpt.isPresent()){
                Coupon coupon = couponOpt.get();
                coupon.setQuantity(coupon.getQuantity()-1);
                couponRepo.save(coupon);
            }
        }

        List<SmsUserInfo> smsUserList = new ArrayList<>();
        smsUserList.add(new SmsUserInfo(order.getShippingName(), order.getShippingPhone(), order.getGender(),order.getGmtCreate(),
                order.getGmtCreate(),Utility.getCurrentDate(),Utility.getCurrentDate(), order.getLocation()));
        for(OrderDetail item : order.getOrderDetails()){
            if(!item.getPhone().isEmpty() && !item.getPhone().equals(order.getShippingPhone()) && order.getLocation().equals("STORE")){
                smsUserList.add(new SmsUserInfo(item.getName(), item.getPhone(), item.getGender(),item.getGmtCreate(),item.getGmtCreate(),
                        Utility.getCurrentDate(),Utility.getCurrentDate(), order.getLocation()));
            }
        }
        for(SmsUserInfo userInfo : smsUserList){
            if(userInfo.getPhone().replace(" ","").length() < 10){
                continue;
            }
            Optional<SmsUserInfo> userInfoDBOtp = smsUserInfoRepo.findByPhone(userInfo.getPhone());
            if(userInfoDBOtp.isPresent()){
                userInfo.setId(userInfoDBOtp.get().getId());
            }
        }
        smsUserInfoRepo.saveAll(smsUserList);
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

}
