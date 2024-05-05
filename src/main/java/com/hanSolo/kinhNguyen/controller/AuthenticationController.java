package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.cacheCenter.CommonCache;
import com.hanSolo.kinhNguyen.models.*;
import com.hanSolo.kinhNguyen.repository.*;
import com.hanSolo.kinhNguyen.response.GeneralResponse;
import com.hanSolo.kinhNguyen.response.GenericResponse;
import com.hanSolo.kinhNguyen.response.MemberResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    @Autowired private LensProductRepository lensProductRepo;
    @Autowired private SmsQueueRepository smsQueueRepo;

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
    public GeneralResponse<Order> saveOrder(@RequestBody final Order order, final HttpServletRequest request) throws ServletException, ParseException {
        final Claims claims = (Claims) request.getAttribute("claims");

        Member currMem = CommonCache.LOGIN_MEMBER_LIST.getOrDefault(claims.get("sub"),null);
        if(!currMem.getStatus()){
            return new GeneralResponse(null, Utility.FAIL_ERRORCODE,"member not exist or disable");
        }

        order.setMember(currMem);
/*
        if(order.getId() == 0){
            order.setGmtCreate(Utility.getCurrentDate());
        }*/
        order.setGmtModify(Utility.getCurrentDate());
        // 0. sms notify payment
        manageSmsNotifyOrder(order);

        // 0.1 manage concurrency on orderDetail lv.
        //manageConcurrencyOrder(order);

        // 1. save order
        Order or = orderRepo.save(order);

        // 1.1
        handleOrderCache(or);

        // 2. update coupon
        updateCouponQuantity(order,order.getCurrentCouponCode(),order.getCouponCode(), Utility.COUPON_TYPE_BILL);

        // 3. save SmsUserInfo
        manageSmsUserInfo(order);

        // 4. save SpecificSmsUserInfo
        manageSpecificSmsUserInfo(order, or);

        // 5. used coupon

        // 6. manage lens product
        manageLensProduct(or);

        GeneralResponse<Order> response = or == null ? new GeneralResponse("",Utility.FAIL_ERRORCODE,"save order fail") : new GeneralResponse(or,Utility.SUCCESS_ERRORCODE,"save order success");
        return response;
    }

    private void handleOrderCache(Order or) {
    }

    /**
     * check the concurrency on orderDetail level
     * @param order
     */
    private void manageConcurrencyOrder(Order order) {
        if(order.getId() == 0){
            return;
        }

        if(!CommonCache.MULTIPLE_EDIT_CONCURRENCE){
            return;
        }

        Order tempOrder = CommonCache.ORDER_LIST.get(order.getId());
        if(tempOrder == null){
            tempOrder = orderRepo.findById(order.getId()).get();
        }
        if(tempOrder != null){
            //Order dbOrder = cacheOrder;
            List<OrderDetail> small,big;
            if(order.getOrderDetails().size() <= tempOrder.getOrderDetails().size()){
                small = order.getOrderDetails(); // orderDetail from client
                big   = tempOrder.getOrderDetails(); // orderDetail from db
            }else{
                small = tempOrder.getOrderDetails();
                big   = order.getOrderDetails();
            }
            //small = order.getOrderDetails().size() <= tempOrder.getOrderDetails().size() ? order.getOrderDetails() : tempOrder.getOrderDetails();
            //big   = order.getOrderDetails().size() > tempOrder.getOrderDetails().size() ? order.getOrderDetails() : tempOrder.getOrderDetails();
            order.setOrderDetails(merge2OrderDetailList(small,big));
            CommonCache.ORDER_LIST.put(order.getId(),order);
        }
    }

    /**
     * merge 2 orderDetail lists
     *  1. if there is new orderDetail, it won't be deleted by overwrite.
     *  2. if there is new data on amount of frame and lens, they won't be deleted by overwrite.
     * @param small
     * @param big
     * @return
     */
    private List<OrderDetail> merge2OrderDetailList(List<OrderDetail> small, List<OrderDetail> big){
        // update orderDetail
        for(OrderDetail item : small){
            OrderDetail bigDetail = big.stream().
                    filter(detail -> item.getId().intValue() == detail.getId().intValue()).findFirst().orElse(null);
            if(null != bigDetail){
                OrderDetail mergedDetail = Utility.mergeObjects(item,bigDetail);
                big.replaceAll(od -> od.getId().intValue() == item.getId().intValue() ? mergedDetail : od);
            }
        }
        return big;
    }

    private void manageSpecificSmsUserInfo(Order order,Order or) throws ParseException {
        Optional<SpecificSmsUserInfo> specSmsDBOtp = specificSmsUserInfoRepo.findByPhone(order.getShippingPhone());
        if(order.getSpecificJobId() != 0 ){
            SpecificSmsUserInfo specSms;
            if(specSmsDBOtp.isEmpty()){ // phone doesn't exist in SpecificSmsUserInfo
                specSms = new SpecificSmsUserInfo();
                specSms.setGender(order.getGender());
                specSms.setLastSendSmsDate(order.getGmtCreate());
                specSms.setOrderCreateDate(order.getGmtCreate());
                specSms.setGmtCreate(Utility.getCurrentDate());
                specSms.setPhone(order.getShippingPhone());
                specSms.setName(order.getShippingName());
                specSms.setJobIdList("");
                specSms.setLocation("STORE");
            }else{ // phone existed in SpecificSmsUserInfo.
                specSms = specSmsDBOtp.get();
              /*  if( 0 ==  order.getId()){ // meaning new order,update old specSms
                    specSms.setJobIdList("");
                    specSms.setOrderCreateDate(order.getGmtCreate());
                }*/
                int specSmsOrderId = specSms.getOrderId() == null ? 0 : specSms.getOrderId();
                if(or.getId() > specSmsOrderId){ // patient come back, reset recheck config.
                    specSms.setLastSendSmsDate(order.getGmtCreate());
                    specSms.setOrderCreateDate(order.getGmtCreate());
                    specSms.setJobIdList("");

                }
            }
            specSms.setAddress(order.getShippingAddress());
            specSms.setGender(order.getGender());
            specSms.setJobIdToRun(order.getSpecificJobId().toString());
            specSms.setGmtModify(Utility.getCurrentDate());
            specSms.setOrderId(or.getId());
            specSms.setClientCode(or.getClientCode());
            specSms.setShopCode(or.getShopCode());
            specificSmsUserInfoRepo.save(specSms);
        }else{
            specSmsDBOtp.ifPresent(x -> specificSmsUserInfoRepo.delete(x));
        }
    }

    private void manageSmsUserInfo(Order order) throws ParseException {
        List<SmsUserInfo> smsUserList = new ArrayList<>();
        smsUserList.add(new SmsUserInfo(order.getShippingName(), order.getShippingPhone(), order.getGender(),order.getGmtCreate(),
                order.getGmtCreate(),Utility.getCurrentDate(),Utility.getCurrentDate(), order.getLocation(),
                order.getShippingAddress(),order.getAreaCode()));
        for(OrderDetail item : order.getOrderDetails()){
            if(!item.getPhone().isEmpty() && !item.getPhone().equals(order.getShippingPhone()) && order.getLocation().equals("STORE")){
                smsUserList.add(new SmsUserInfo(item.getName(), item.getPhone(), item.getGender(),item.getGmtCreate(),item.getGmtCreate(),
                        Utility.getCurrentDate(),Utility.getCurrentDate(), order.getLocation(),item.getAddress(), order.getAreaCode()));
            }
            updateCouponQuantity(order,item.getCurrentFrameDiscountCode(),item.getFrameDiscountCode(), Utility.COUPON_TYPE_FRAME);
            updateCouponQuantity(order,item.getCurrentLensDiscountCode(),item.getLensDiscountCode(), Utility.COUPON_TYPE_LENS);
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
            smsUserInfo.setClientCode(order.getClientCode());
            smsUserInfo.setShopCode(order.getShopCode());
            smsUserResult.add(smsUserInfo);
        }
        smsUserInfoRepo.saveAll(smsUserResult);
    }

    private void manageSmsNotifyOrder(Order order) throws ParseException {
        if(!order.getDoneSmsPaymentNotify() && order.getShippingPhone().replace(" ","").length() > 9){
            SmsJob job = CommonCache.SMS_JOB_LIST.get(Utility.SMS_JOB_NOTIFYORDER);
            if(job != null){
                SmsQueue smsQueue = generateSmsQueue(job, order);
                smsQueueRepo.save(smsQueue);
                order.setDoneSmsPaymentNotify(true);
            }
        }
    }

    private SmsQueue generateSmsQueue(SmsJob job, Order order) throws ParseException {
        SmsQueue smsQueue = new SmsQueue();
        smsQueue.setJobId(job.getId());
        smsQueue.setGmtCreate(Utility.getCurrentDate());
        smsQueue.setGmtModify(Utility.getCurrentDate());
        smsQueue.setContent(job.getMsgContentTemplate() +" ["+ RandomStringUtils.randomAlphanumeric(4)+"]");
        smsQueue.setGender(order.getGender());
        smsQueue.setStatus(Utility.SMS_QUEUE_INIT);
        smsQueue.setReceiverName(order.getShippingName());
        smsQueue.setReceiverPhone(order.getShippingPhone());
        smsQueue.setWeight(job.getWeight());
        smsQueue.setClientCode(order.getClientCode());
        smsQueue.setShopCode(order.getShopCode());
        smsQueue.setJobType(job.getJobType());

        return smsQueue;
    }

    private void manageLensProduct(Order order) throws ParseException {
        if(!order.getOrderDetails().isEmpty()){
            String lensDetail = "";
            String extInfo = "";
            for( OrderDetail detail : order.getOrderDetails()){
                if(!detail.getLensNote().isBlank() && detail.getLensPrice() > 0){
                    lensDetail = buildLensDetail(detail);
                    List<LensProduct> lensProductList = lensProductRepo.findFirstByLensDetailAndSellPrice(
                                                        lensDetail, detail.getLensPrice());
                    if(lensProductList.isEmpty()){
                        extInfo = order.getId() + "-" + detail.getId();
                        LensProduct lensProduct = new LensProduct(detail.getGmtCreate(),
                                detail.getGmtModify(),
                                detail.getLensNote(),
                                lensDetail,
                                extInfo,
                                detail.getLensPrice()
                        );
                        lensProduct.setClientCode(order.getClientCode());
                        lensProduct.setShopCode(order.getShopCode());

                        if(CommonCache.LENS_PRODUCT_LIST.containsKey(extInfo)){
                            LensProduct lp = CommonCache.LENS_PRODUCT_LIST.get(extInfo);
                            lensProduct.setGmtModify(Utility.getCurrentDate());
                            lensProduct.setLensNote(lp.getLensNote());
                            lensProduct.setLensDetail(lp.getLensDetail());
                            lensProduct.setSellPrice(lp.getSellPrice());
                        }
                        lensProduct = lensProductRepo.save(lensProduct);
                        if(CommonCache.LENS_PRODUCT_LIST.size() == Utility.LENS_PRODUCT_LIST_SIZE){
                            CommonCache.LENS_PRODUCT_LIST.clear();
                        }
                        CommonCache.LENS_PRODUCT_LIST.put(lensProduct.getExtInfo(), lensProduct);
                    }
                }
            }
        }
    }

    private String buildLensDetail(OrderDetail detail){
        String reading = (detail.getReading() == null ? false : detail.getReading() ) ? ", đọc sách" : "";
        String lensDeatil = "("+detail.getOdSphere() +" "+ detail.getOdCylinder()+" "+detail.getOdPrism()+")" +
                "("+detail.getOsSphere() +" "+ detail.getOsCylinder()+" "+detail.getOsPrism()+")" +
                reading;
        return lensDeatil;
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

    ////
    @RequestMapping(value = "syncOrder", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse syncOrder(@RequestBody final Order order, final HttpServletRequest request) throws ServletException, ParseException {
        final Claims claims = (Claims) request.getAttribute("claims");
        Optional<Member> memOpt = memberRepo.findByPhoneAndStatus(claims.get("sub")+"", Utility.ACTIVE_STATUS);
        if (memOpt.isEmpty() ) {
            return new GenericResponse(null, Utility.FAIL_ERRORCODE,"member not exist or disable");
        }
        order.setMember(memOpt.get());
        Order or = orderRepo.save(order);

        GenericResponse response = or == null ? new GenericResponse("",Utility.FAIL_ERRORCODE,"save order fail") : new GenericResponse(or.getId()+"",Utility.SUCCESS_ERRORCODE,"save order success");
        return response;
    }


    private void updateCouponQuantity(Order order,String currentCode, String newCode, String couponType){
        List<Coupon> couponList;
        List<Coupon> result;
        // revert coupon
        if(!StringUtils.hasText(newCode)){
            if(StringUtils.hasText(currentCode)){

                couponList = couponRepo.findByCodeAndCouponTypeAndQuantityGreaterThanAndClientCodeOrderByGmtCreateDesc
                        (currentCode,couponType,0, order.getClientCode());

                result = Utility.filterCoupon(couponList);

                if(!result.isEmpty()){
                    Coupon currentCoupon = result.get(0);
                    currentCoupon.setQuantity(currentCoupon.getQuantity()+1);
                    couponRepo.save(currentCoupon);
                }
            }
            return;
        }


        //
        couponList = couponRepo.findByCodeAndCouponTypeAndQuantityGreaterThanAndClientCodeOrderByGmtCreateDesc
                (newCode,couponType,0, order.getClientCode());

        result = Utility.filterCoupon(couponList);
        if(result.isEmpty()){
            return;
        }
        Coupon coupon = result.get(0);
        // new order
        if(order.getId() == 0){
            coupon.setQuantity(coupon.getQuantity()-1);
            couponRepo.save(coupon);
        }else if( StringUtils.hasText(newCode) && !newCode.equals(currentCode)){
            // update existing order with new code and subtract amount of new code
            coupon.setQuantity(coupon.getQuantity()-1);
            couponRepo.save(coupon);
            if(StringUtils.hasText(currentCode)){
                couponList = couponRepo.findByCodeAndCouponTypeAndQuantityGreaterThanAndClientCodeOrderByGmtCreateDesc
                        (currentCode,couponType,0, order.getClientCode());

                result = Utility.filterCoupon(couponList);
                if(!result.isEmpty()){
                    Coupon currentCoupon = result.get(0);
                    currentCoupon.setQuantity(currentCoupon.getQuantity()+1);
                    couponRepo.save(currentCoupon);
                }
            }
        }

    }

}
