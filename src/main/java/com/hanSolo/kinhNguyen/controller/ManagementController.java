package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.*;
import com.hanSolo.kinhNguyen.repository.*;
import com.hanSolo.kinhNguyen.response.*;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mgnt")
public class ManagementController {

    @Autowired private CategoryRepository categoryRepo;
    @Autowired private SupplierRepository supplierRepo;
    @Autowired private ProductRepository prodRepo;
    @Autowired private BannerRepository bannerRepo;
    @Autowired private MemberRepository memberRepo;
    @Autowired private CouponRepository couponRepo;
    @Autowired private ArticleRepository articleRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private OrderDetailRepository orderDetailRepo;
    @Autowired private UsedCouponsRepository usedCouponsRepo;
    @Autowired private BizExpenseRepository bizExpenseRepo;
    @Autowired private CustomerSourceRepository customerSourceRepo;
    @Autowired private BizReportRepository bizReportRepo;
    @Autowired private SmsUserInfoRepository smsUserInfoRepo;
    @Autowired private SmsQueueRepository smsQueueRepo;
    @Autowired private SmsJobRepository smsJobRepo;
    @Autowired private SpecificSmsUserInfoRepository specificSmsUserInfoRepo;
    @Autowired private KeyManagementRepository keyManagementRepo;
    @Autowired private CustomerSourceReportRepository customerSourceReportRepo;
    @Autowired private ContractRepository contractRepo;
    @Autowired private SalaryRepository salaryRepo;

    @Autowired private Environment env;

    ////////////////////////////category section//////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getAllCategories", method = RequestMethod.GET)
    public List<Category> getAllCategories(final HttpServletRequest request) {
        return categoryRepo.findByOrderByIdDesc();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "upsertCategory", method = RequestMethod.POST)
    public CategoryResponse upsertCategory(@RequestBody final Category cate, final HttpServletRequest request) {
        Category newCate = categoryRepo.save(cate);
        return new CategoryResponse(newCate,Utility.SUCCESS_ERRORCODE,"Success");
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "deleteCategory", method = RequestMethod.POST)
    public GenericResponse deleteCategory(@RequestBody final Category cate, final HttpServletRequest request)  {
        categoryRepo.delete(cate);
        return new GenericResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    ////////////////////////////supplier section//////////////////////////

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getAllSuppliers", method = RequestMethod.GET)
    public List<Supplier> getAllSuppliers(final HttpServletRequest request) {
        return supplierRepo.findByOrderByGmtModifyDesc();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "upsertSupplier", method = RequestMethod.POST)
    public SupplierResponse upsertSupplier(@RequestBody final Supplier supplier, final HttpServletRequest request) throws ServletException, ParseException {
        if(supplier.getId() == 0){
            supplier.setGmtCreate(Utility.getCurrentDate());
        }
        supplier.setGmtModify(Utility.getCurrentDate());
        Supplier newSupplier = supplierRepo.save(supplier);
        return new SupplierResponse(newSupplier,Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// product section ///////////////////////////////
    @RequestMapping(value = "getProductsForMgnt/{cateId}/{amount}", method = RequestMethod.GET)
    public List<Product> getProductsForMgnt(@PathVariable final int cateId, @PathVariable final int amount, final HttpServletRequest request) throws ServletException {
        List<Product> productList;
            if(cateId==0){
                if(amount==Utility.FIRTST_TIME_LOAD_SIZE){
                    productList =  prodRepo.findFirst100ByOrderByGmtModifyDesc();
                }else{
                    productList =  prodRepo.findByOrderByGmtModifyDesc();
                }
            }else{
                if(amount==Utility.FIRTST_TIME_LOAD_SIZE){
                    productList =  prodRepo.findFirst100ByCategories_IdOrderByGmtModifyDesc(cateId);
                }else{
                    productList =  prodRepo.findByCategories_IdOrderByGmtModifyDesc(cateId);
                }
            }

        return productList;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "upsertProduct", method = RequestMethod.POST)
    public GenericResponse updateProducts(@RequestBody final Product product, final HttpServletRequest request) throws ServletException, ParseException {
        if(product.getId() == 0){
            product.setGmtCreate(Utility.getCurrentDate());
        }
        product.setGmtModify(Utility.getCurrentDate());

        prodRepo.save(product);

        return new GenericResponse("upsert_product_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "getProductById/{prodId}", method = RequestMethod.GET)
    public Product getProductById(@PathVariable final int prodId, final HttpServletRequest request) throws ServletException {
        return	prodRepo.findById(prodId).get();
    }

    @RequestMapping(value = "updateProductStatus", method = RequestMethod.POST)
    public GenericResponse updateProductStatus(@RequestBody final Product product) throws ParseException, InterruptedException {
        prodRepo.updateStatusAndGmtModifyById(product.getStatus(),Utility.getCurrentDate(),product.getId());
        //TimeUnit.SECONDS.sleep( 4);
        return new GenericResponse("upsert_product_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// banner section ///////////////////////////////

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getBannerForMgnt", method = RequestMethod.GET)
    public List<Banner> getBanner(final HttpServletRequest request) throws ServletException {
        return bannerRepo.findByOrderByGmtModifyDesc();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "upsertBanner", method = RequestMethod.POST)
    public GenericResponse updateBanner(@RequestBody final Banner banner, final HttpServletRequest request) throws ServletException, ParseException {
        if(banner.getId() == 0){
            banner.setGmtCreate(Utility.getCurrentDate());
        }
        banner.setGmtModify(Utility.getCurrentDate());

        bannerRepo.save(banner);
        return new GenericResponse("upsert_banner_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "deleteBanner", method = RequestMethod.POST)
    public GenericResponse deleteBanner(@RequestBody final Banner banner, final HttpServletRequest request) {
        bannerRepo.delete(banner);
        return new GenericResponse("delete_banner_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// Biz expense section ///////////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getBizExpenseForMgnt/{amount}", method = RequestMethod.GET)
    public List<BizExpense> getBizExpenseForMgnt(@PathVariable final int amount, final HttpServletRequest request) {

        List<BizExpense> bizExpenseList;
        final Claims claims = (Claims) request.getAttribute("claims");
        if(((List<String>) claims.get("roles")).contains(Utility.ACCOUNTANT_ROLE)){
            if(amount==Utility.FIRTST_TIME_LOAD_SIZE){
                bizExpenseList =  bizExpenseRepo.findFirst100ByOrderByGmtCreateDesc();
            }else{
                bizExpenseList = bizExpenseRepo.findByOrderByGmtCreateDesc();
            }
        }else{
            if(amount==Utility.FIRTST_TIME_LOAD_SIZE){
                bizExpenseList =  bizExpenseRepo.findFirst100ByOwnerPhoneOrderByGmtCreateDesc(claims.get("sub").toString());
            }else{
                bizExpenseList = bizExpenseRepo.findByOwnerPhoneOrderByGmtCreateDesc(claims.get("sub").toString());
            }
        }

        return bizExpenseList;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "upsertBizExpense", method = RequestMethod.POST)
    public GenericResponse upsertBizExpense(@RequestBody final BizExpense bizExpense, final HttpServletRequest request) throws ParseException {
        bizExpense.setGmtModify(Utility.getCurrentDate());
        bizExpenseRepo.save(bizExpense);
       // TimeUnit.SECONDS.sleep( 4);
        return new GenericResponse("upsert_banner_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "deleteBizExpense", method = RequestMethod.POST)
    public GenericResponse deleteBizExpense(@RequestBody final BizExpense bizExpense, final HttpServletRequest request)  {
        bizExpenseRepo.delete(bizExpense);
        return new GenericResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "updateBizExpenseStatus", method = RequestMethod.POST)
    public GenericResponse updateBizExpenseStatus(@RequestBody final BizExpense bizExpense, final HttpServletRequest request) throws ParseException {
        final Claims claims = (Claims) request.getAttribute("claims");
        if(((List<String>) claims.get("roles")).contains(Utility.SUPER_ACCOUNTANT_ROLE)){
            bizExpenseRepo.updateStatusAndGmtModifyById(bizExpense.getStatus(),Utility.getCurrentDate(),bizExpense.getId());
        }
        return new GenericResponse("upsert_bizExpense_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////////Member section/////////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getMemberForMgnt/{amount}", method = RequestMethod.GET)
    public List<Member> getMemberForMgnt(@PathVariable final int amount, final HttpServletRequest request) throws ServletException {
        List<Member> memberList;
            if(amount==Utility.FIRTST_TIME_LOAD_SIZE){
                memberList =  memberRepo.findFirst100ByOrderByGmtCreateDesc();
            }else{
                memberList = memberRepo.findByOrderByGmtCreateDesc();
            }
            memberList.forEach(item -> item.setPass(""));
        return memberList;
    }

    @RequestMapping(value = "upsertMember", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse updateMe(@RequestBody final Member member,final HttpServletRequest request) throws ServletException, ParseException {
        Optional<Member>  memberOpt = memberRepo.findById(member.getId());
        if(memberOpt.isPresent()){
            member.setPass(memberOpt.get().getPass());
            member.setGmtModify(Utility.getCurrentDate());
        }
        memberRepo.save(member);

        return new GenericResponse("upsert_member_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// Coupon section ///////////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getAllCoupons", method = RequestMethod.GET)
    public List<Coupon> getAllCoupons(final HttpServletRequest request) throws ServletException {
        return couponRepo.findAllByOrderByGmtCreateDesc();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "upsertCoupon", method = RequestMethod.POST)
    public CouponResponse upsertCoupon(@RequestBody final Coupon coupon, final HttpServletRequest request) throws ServletException, ParseException {
        if(coupon.getId() == 0){
            coupon.setGmtCreate(Utility.getCurrentDate());
        }
        coupon.setGmtModify(Utility.getCurrentDate());
        return new CouponResponse(couponRepo.save(coupon),Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "deleteCoupon", method = RequestMethod.POST)
    public GenericResponse deleteCoupon(@RequestBody final Coupon coupon, final HttpServletRequest request) throws ServletException {
        couponRepo.delete(coupon);
        return new GenericResponse("delete_coupon_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "loadUsedCouponHistory", method = RequestMethod.POST)
    public List<UsedCoupons> loadUsedCouponHistory(final HttpServletRequest request) throws ServletException {
        return usedCouponsRepo.findByOrderByOrderDateDesc();
    }

    //////////////////////////// customer Source section ///////////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getAllCustomerSource", method = RequestMethod.GET)
    public List<CustomerSource> getAllCustomerSource(final HttpServletRequest request) throws ServletException {
        return customerSourceRepo.findAllByOrderByGmtCreateDesc();
    }

    @RequestMapping(value = "upsertCustomerSource", method = RequestMethod.POST)
    public CustomerSourceResponse upsertCustomerSource(@RequestBody final CustomerSource customerSource, final HttpServletRequest request) throws ParseException {
        if(customerSource.getId() == 0){
            customerSource.setGmtCreate(Utility.getCurrentDate());
        }
        customerSource.setGmtModify(Utility.getCurrentDate());
        return new CustomerSourceResponse(customerSourceRepo.save(customerSource),Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "upsertCustomerSourceReport", method = RequestMethod.POST)
    public GenericResponse upsertCustomerSourceReport(@RequestBody final CustomerSourceReport customerSourceReport, final HttpServletRequest request) throws ParseException {
        List<CustomerSourceReport> csrList = new ArrayList<>();
        if(customerSourceReport.getId() == 0){
            List<CustomerSource> csList = customerSourceRepo.findAll();
            for(CustomerSource cs : csList){
                csrList.add(new CustomerSourceReport(Utility.getCurrentDate(),
                        Utility.getCurrentDate(),
                        customerSourceReport.getYear(),
                        customerSourceReport.getMonth(),
                        cs.getId(),
                        0
                ));
            }
            customerSourceReportRepo.saveAll(csrList);
        }else{
            customerSourceReportRepo.save(customerSourceReport);
        }

        return new GenericResponse("Success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "getAllCustomerSourceReport", method = RequestMethod.GET)
    public List<CustomerSourceReport> getAllCustomerSourceReport(final HttpServletRequest request) throws ServletException {
        return customerSourceReportRepo.findByOrderByYearDescMonthDescCustomerSourceIdAsc();
    }

    @RequestMapping(value = "calCustomerSourceReport", method = RequestMethod.POST)
    public GenericResponse calCustomerSourceReport(@RequestBody final CustomerSourceReport customerSourceReport,final HttpServletRequest request) throws ServletException, ParseException {
        Date startDate = Utility.getFirstDateOfMonth(customerSourceReport.getYear(),customerSourceReport.getMonth());
        Date endDate = Utility.getLastDateOfMonth(customerSourceReport.getYear(),customerSourceReport.getMonth());
        List<CustomerSource> csList = customerSourceRepo.findAll();
        List<CustomerSourceReport> csrList = customerSourceReportRepo.findByYearAndMonth(customerSourceReport.getYear(),customerSourceReport.getMonth());

        for(CustomerSource cs : csList){
            long count = orderRepo.countByGmtCreateBetweenAndCusSource(startDate,endDate,cs.getId());
            CustomerSourceReport csr = csrList.stream().filter(r -> r.getCustomerSourceId() == cs.getId())
                    .findAny()
                    .orElse(null);
            if(csr != null){
                csr.setCount((int) count);
            }
        }
        long nullCount = orderRepo.countByGmtCreateBetweenAndCusSource(startDate,endDate,null);
        long id0Count = orderRepo.countByGmtCreateBetweenAndCusSource(startDate,endDate,0);
        // 8= unknown
        CustomerSourceReport csr = csrList.stream().filter(r -> r.getCustomerSourceId() == 8).findAny().orElse(null);
        if(csr != null){
            csr.setCount((int) (csr.getCount() + nullCount + id0Count));
        }

        customerSourceReportRepo.saveAll(csrList);
        return new GenericResponse("Success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// biz report section ///////////////////////////////
    @RequestMapping(value = "getAllBizReport", method = RequestMethod.GET)
    public List<BizReport> getAllBizReport(final HttpServletRequest request) throws ServletException {
        return bizReportRepo.findByOrderByGmtCreateDesc();
    }

    @RequestMapping(value = "upsertBizReport", method = RequestMethod.POST)
    public BizReportResponse upsertBizReport(@RequestBody final BizReport bizReport, final HttpServletRequest request) throws ParseException {
        if(bizReport.getId() == 0){
            bizReport.setGmtCreate(Utility.getCurrentDate());
        }
        bizReport.setGmtModify(Utility.getCurrentDate());
        return new BizReportResponse(bizReportRepo.save(bizReport),Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "calculateReport", method = RequestMethod.POST)
    public BizReportResponse calculateReport(@RequestBody final BizReport bizReport, final HttpServletRequest request) throws ParseException {
        bizReport.setGmtModify(Utility.getCurrentDate());

        Date startDate = Utility.getFirstDateOfMonth(bizReport.getYear(),bizReport.getMonth());
        Date endDate = Utility.getLastDateOfMonth(bizReport.getYear(),bizReport.getMonth());
        List<BizExpense> expenses = bizExpenseRepo.findByGmtCreateBetween(startDate,endDate);
        int expAmount = 0;
        for(BizExpense exp : expenses){
            expAmount += exp.getAmount() != null ? exp.getAmount() : 0;
        }
        bizReport.setOutcome(expAmount);

        List<Order> orderList = orderRepo.findByGmtCreateBetween(startDate,endDate);
        int incomeAmount = 0;
        int discountAmount = 0;
        int lensQty = 0;
        int frameQty = 0;
        for(Order or : orderList){
            int amount = 0;
            int disAmount = 0;
            for(OrderDetail orderDetail : or.getOrderDetails()){
                int lensPrice = orderDetail.getLensPrice() != null ? orderDetail.getLensPrice() : 0;
                int otherPrice = orderDetail.getOtherPrice() != null ? orderDetail.getOtherPrice() : 0;

                disAmount += orderDetail.getFramePriceAtThatTime()*orderDetail.getFrameDiscountAmount()/100
                             + lensPrice*orderDetail.getLensDiscountAmount()/100;
                amount += orderDetail.getFramePriceAtThatTime() + lensPrice + otherPrice - disAmount;

                if(orderDetail.getFramePriceAtThatTime() > 1000){
                    frameQty += 1;
                }
                if(orderDetail.getLensPrice() != null && orderDetail.getLensPrice() > 1000){
                    lensQty += 1;
                }
            }
            discountAmount += disAmount + amount*or.getCouponDiscount()/100;
            incomeAmount += amount - amount*or.getCouponDiscount()/100;
        }
        bizReport.setFrameQuantity(frameQty);
        bizReport.setLensQuantity(lensQty);
        bizReport.setIncome(incomeAmount);
        bizReport.setOrderQuantity(orderList.size());
        bizReport.setDiscountAmount(discountAmount);

        return new BizReportResponse(bizReportRepo.save(bizReport),Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "deleteBizReport", method = RequestMethod.POST)
    public GenericResponse deleteBizReport(@RequestBody final BizReport bizReport) {
        bizReportRepo.delete(bizReport);
        return new GenericResponse("delete_bizReport_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// blog/article section ///////////////////////////////
    @RequestMapping(value = "getArticlesForMgnt/{amount}", method = RequestMethod.GET)
    public List<Article> getArticlesForMgnt(@PathVariable final int amount) throws ServletException {
        List<Article> articleList;
        if(amount==Utility.FIRTST_TIME_LOAD_SIZE){
            articleList =  articleRepo.findFirst100ByOrderByGmtModifyDesc();
        }else{
            articleList = articleRepo.findByOrderByGmtModifyDesc();
        }
        return articleList;
    }

    @RequestMapping(value = "upsertArticle", method = RequestMethod.POST)
    public GenericResponse updateArticle(@RequestBody final Article article) throws ServletException, ParseException {
        if(article.getId() == 0){
            article.setGmtCreate(Utility.getCurrentDate());
        }
        article.setGmtModify(Utility.getCurrentDate());
        articleRepo.save(article);
        return new GenericResponse("upsert_article_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getArticleById/{articleId}", method = RequestMethod.GET)
    public Article getArticleById(@PathVariable final int articleId) throws ServletException {
        return	articleRepo.findById(articleId).get();
    }

    /////////////////////////Orders section///////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getOrdersForMgnt/{amount}", method = RequestMethod.GET)
    public List<Order> getOrdersForMgnt(@PathVariable final int amount, final HttpServletRequest request) {
        List<Order> orderList ;
        if(amount==Utility.FIRTST_TIME_LOAD_SIZE){
            orderList =  orderRepo.findFirst100ByOrderByGmtCreateDesc();
        }else{
            orderList = orderRepo.findAllByOrderByGmtCreateDesc();
        }
        return orderList;
    }

    @RequestMapping(value = "updateOrderStatus", method = RequestMethod.POST)
    public GenericResponse updateOrder(@RequestBody final Order order) throws ParseException {
        orderRepo.updateStatusAndGmtModifyById(order.getStatus(),Utility.getCurrentDate(),order.getId());
        return new GenericResponse("upsert_order_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "getOrderById/{orderId}", method = RequestMethod.GET)
    public Order getOrderById(@PathVariable final int orderId) throws ServletException {
        Order or = orderRepo.findById(orderId).get();
        return	orderRepo.findById(orderId).get();
    }

    @RequestMapping(value = "deleteOrder", method = RequestMethod.POST)
    public GenericResponse deleteOrder(@RequestBody final Order order, final HttpServletRequest request) throws ServletException {
        orderRepo.delete(order);
        return new GenericResponse("delete_order_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "getOnePrescription/{orderDetailId}", method = RequestMethod.GET)
    public Order getOrderDetailById(@PathVariable final int orderDetailId) throws ServletException {
        OrderDetail orderDetail = orderDetailRepo.findById(orderDetailId).get();
        Order order = orderRepo.findById(orderDetail.getOrderId()).get();
        order.setOrderDetails(new ArrayList<>());
        order.getOrderDetails().add(orderDetail);

        return	order;
    }

    @RequestMapping(value = "getPrescriptionsForMgnt/{amount}", method = RequestMethod.GET)
    public List<OrderDetail> getPrescriptionsForMgnt(@PathVariable final int amount, final HttpServletRequest request) {
        List<OrderDetail> orderDetailListList ;
        if(amount==Utility.FIRTST_TIME_LOAD_SIZE){
            orderDetailListList =  orderDetailRepo.findFirst100ByNameNotAndPhoneNotOrderByGmtCreateDesc("","");
        }else{
            orderDetailListList = orderDetailRepo.findByNameNotAndPhoneNotOrderByGmtCreateDesc("","");
        }
        return orderDetailListList;
    }

    @RequestMapping(value = "updateCusSource", method = RequestMethod.POST)
    public GenericResponse updateCusSource(@RequestBody final Order order) throws ParseException {
        orderRepo.updateGmtModifyAndCusSourceById(Utility.getCurrentDate(),order.getCusSource(),order.getId());

        Optional<CustomerSource>  customerSourceOpt = customerSourceRepo.findById(order.getCusSource());
        if(customerSourceOpt.isPresent()){
            CustomerSource customerSource = customerSourceOpt.get();
            customerSource.setCount(customerSource.getCount() + 1);
            customerSourceRepo.save(customerSource);
        }

        if(order.getCurrentCusSource() != null){
            Optional<CustomerSource>  currentCustomerSourceOpt = customerSourceRepo.findById(order.getCurrentCusSource());
            if(currentCustomerSourceOpt.isPresent()){
                CustomerSource currentCustomerSource = currentCustomerSourceOpt.get();
                currentCustomerSource.setCount(currentCustomerSource.getCount() - 1);
                customerSourceRepo.save(currentCustomerSource);
            }
        }

        return new GenericResponse("upsert_order_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "saveMultipleOrders", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse saveMultipleOrders(@RequestBody final List<Order> orders, final HttpServletRequest request) throws ServletException, ParseException {
        orderRepo.saveAll(orders);
        GenericResponse response =  new GenericResponse("Success",Utility.SUCCESS_ERRORCODE,"save order success");
        return response;
    }

    //////////////////////////// smsUserInfo section /////////////////////////////
    @RequestMapping(value = "getSmsUserInfoForMgnt/{amount}", method = RequestMethod.GET)
    public List<SmsUserInfo> getSmsUserInfoForMgnt(@PathVariable final int amount, final HttpServletRequest request) {
        List<SmsUserInfo> SmsUserInfoList ;
        if(amount == Utility.FIRTST_TIME_LOAD_SIZE){
            SmsUserInfoList =  smsUserInfoRepo.findFirst100ByOrderByGmtCreateDesc();
        }else{
            SmsUserInfoList = smsUserInfoRepo.findAllByOrderByGmtCreateDesc();
        }
        return SmsUserInfoList;
    }

    @RequestMapping(value = "upsertSmsUserInfo", method = RequestMethod.POST)
    public SmsUserInfoResponse upsertSmsUserInfo(@RequestBody final SmsUserInfo one, final HttpServletRequest request) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
        }
        one.setGmtModify(Utility.getCurrentDate());

        SmsUserInfo newOne = smsUserInfoRepo.save(one);
        return new SmsUserInfoResponse(newOne,Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "deleteSmsUserInfo", method = RequestMethod.POST)
    public GenericResponse deleteSmsUserInfo(@RequestBody final SmsUserInfo one)  {
        smsUserInfoRepo.delete(one);
        return new GenericResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// sms queue section /////////////////////////////
    @RequestMapping(value = "getSmsQueueForMgnt/{amount}", method = RequestMethod.GET)
    public List<SmsQueue> getSmsQueueForMgnt(@PathVariable final int amount) {
        List<SmsQueue> SmsQueueList ;
        if(amount == Utility.FIRTST_TIME_LOAD_SIZE){
            SmsQueueList =  smsQueueRepo.findFirst100ByOrderByGmtCreateDesc();
        }else{
            SmsQueueList = smsQueueRepo.findAllByOrderByGmtCreateDesc();
        }
        return SmsQueueList;
    }

    @RequestMapping(value = "upsertSmsQueue", method = RequestMethod.POST)
    public SmsQueueResponse upsertSmsQueue(@RequestBody final SmsQueue one) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
        }
        one.setGmtModify(Utility.getCurrentDate());

        SmsQueue newOne = smsQueueRepo.save(one);
        return new SmsQueueResponse(newOne,Utility.SUCCESS_ERRORCODE,"Save sms queue success");
    }

    @RequestMapping(value = "deleteSmsQueue", method = RequestMethod.POST)
    public GenericResponse deleteSmsQueue(@RequestBody final SmsQueue one)  {
        smsQueueRepo.delete(one);
        return new GenericResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "delete100SmsQueue", method = RequestMethod.POST)
    public GenericResponse delete100SmsQueue()  {
        List<SmsQueue> smsQueueList = smsQueueRepo.findFirst100ByStatusOrderByGmtCreateAsc(Utility.SMS_QUEUE_SENT);

        if(!smsQueueList.isEmpty()){
            smsQueueRepo.deleteAll(smsQueueList);
        }
        return new GenericResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// sms Job section /////////////////////////////
    @RequestMapping(value = "getSmsJobForMgnt/{amount}", method = RequestMethod.GET)
    public List<SmsJob> getSmsJobForMgnt(@PathVariable final int amount) {
        return smsJobRepo.findAllByOrderByGmtCreateDesc();
    }

    @RequestMapping(value = "upsertSmsJob", method = RequestMethod.POST)
    public SmsJobResponse upsertSmsJob(@RequestBody final SmsJob one) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
        }
        one.setGmtModify(Utility.getCurrentDate());
        return new SmsJobResponse(smsJobRepo.save(one),Utility.SUCCESS_ERRORCODE,"Save sms queue success");
    }

    @RequestMapping(value = "deleteSmsJob", method = RequestMethod.POST)
    public GenericResponse deleteSmsJob(@RequestBody final SmsJob one)  {
        smsJobRepo.delete(one);
        return new GenericResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "getLastHeartBeatTime", method = RequestMethod.POST)
    public Date getSMSLastTrigger()  {
        return Utility.LAST_SMS_HEARTBEAT_TIME;
    }

    @RequestMapping(value = "getLastPrepareDataTime", method = RequestMethod.POST)
    public Date getPrepareDataLastTrigger()  {
        return Utility.LAST_PREPARE_DATA_HEARTBEAT_TIME;
    }

    //////////////////////////// specific smsUserInfo section /////////////////////////////
    @RequestMapping(value = "getSpecificSmsUserInfoForMgnt/{amount}", method = RequestMethod.GET)
    public List<SpecificSmsUserInfo> getSpecificSmsUserInfoForMgnt(@PathVariable final int amount, final HttpServletRequest request) {
        List<SpecificSmsUserInfo> specificSmsUserInfoList ;
        if(amount == Utility.FIRTST_TIME_LOAD_SIZE){
            specificSmsUserInfoList =  specificSmsUserInfoRepo.findFirst100ByOrderByGmtCreateDesc();
        }else{
            specificSmsUserInfoList = specificSmsUserInfoRepo.findAllByOrderByGmtCreateDesc();
        }
        return specificSmsUserInfoList;
    }

    @RequestMapping(value = "upsertSpecificSmsUserInfo", method = RequestMethod.POST)
    public SpecificSmsUserInfoResponse upsertSpecificSmsUserInfo(@RequestBody final SpecificSmsUserInfo one, final HttpServletRequest request) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
        }
        one.setGmtModify(Utility.getCurrentDate());

        SpecificSmsUserInfo newOne = specificSmsUserInfoRepo.save(one);
        return new SpecificSmsUserInfoResponse(newOne,Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "deleteSpecificSmsUserInfo", method = RequestMethod.POST)
    public GenericResponse deleteSpecificSmsUserInfo(@RequestBody final SpecificSmsUserInfo one)  {
        specificSmsUserInfoRepo.delete(one);
        return new GenericResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// key Management section /////////////////////////////
    @RequestMapping(value = "getAllKeyManagement", method = RequestMethod.GET)
    public List<KeyManagement> getAllKeyManagement() {
        return keyManagementRepo.findAllByOrderByGmtCreateDesc();
    }

    @RequestMapping(value = "upsertKeyManagement", method = RequestMethod.POST)
    public KeyManagementResponse upsertKeyManagement(@RequestBody final KeyManagement one) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
        }
        one.setGmtModify(Utility.getCurrentDate());
        return new KeyManagementResponse(keyManagementRepo.save(one),Utility.SUCCESS_ERRORCODE,"Save key success");
    }

    @RequestMapping(value = "deleteKeyManagement", method = RequestMethod.POST)
    public GenericResponse deleteKeyManagement(@RequestBody final KeyManagement one)  {
        keyManagementRepo.delete(one);
        return new GenericResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "renewKey", method = RequestMethod.POST)
    public KeyManagementResponse renewKey(@RequestBody final KeyManagement one, final HttpServletRequest request) throws UnsupportedEncodingException, ParseException {
        String secretKey = RandomStringUtils.randomAlphanumeric(15);
        Claims claims = (Claims) request.getAttribute("claims");
        String token = Jwts.builder()
                .setSubject("")
                .claim("roles",  claims.get("roles"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + one.getTimeout()*24*60*60*1000))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
        one.setToken(token);one.setSecretKey(secretKey);
        one.setGmtModify(Utility.getCurrentDate());
        return new KeyManagementResponse(keyManagementRepo.save(one),Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// contract Management section /////////////////////////////
    @RequestMapping(value = "getAllContract", method = RequestMethod.GET)
    public List<Contract> getAllContract() {
        return contractRepo.findAllByOrderByGmtCreateDesc();
    }

    @RequestMapping(value = "upsertContract", method = RequestMethod.POST)
    public GeneralResponse<Contract> upsertSalary(@RequestBody final Contract one) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
        }
        one.setGmtModify(Utility.getCurrentDate());
        return new GeneralResponse(contractRepo.save(one),Utility.SUCCESS_ERRORCODE,"Save key success");
    }
    //////////////////////////// salary section /////////////////////////////
    @RequestMapping(value = "getAllSalaryOnePerson", method = RequestMethod.GET)
    public List<Salary> getAllSalaryOnePerson() {
        return salaryRepo.findAllByOrderByGmtCreateDesc();
    }


    @RequestMapping(value = "getAllSalaryOnePerson/{contractId}", method = RequestMethod.GET)
    public List<Salary> getAllSalaryOnePerson(@PathVariable final int contractId, final HttpServletRequest request) {

        List<Salary> salaryList = new ArrayList<>();
        final Claims claims = (Claims) request.getAttribute("claims");
        if(((List<String>) claims.get("roles")).contains(Utility.SUPER_ACCOUNTANT_ROLE)){
           return salaryRepo.findByContractIdOrderByYearDescMonthDesc(String.valueOf(contractId));
        }

        return salaryList;
    }

    @RequestMapping(value = "upsertSalary", method = RequestMethod.POST)
    public GeneralResponse<Salary> upsertContract(@RequestBody final Salary one) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
        }
        one.setGmtModify(Utility.getCurrentDate());
        return new GeneralResponse(salaryRepo.save(one),Utility.SUCCESS_ERRORCODE,"Save key success");
    }

    @RequestMapping(value = "deleteSalary", method = RequestMethod.POST)
    public GenericResponse deleteSalary(@RequestBody final Salary one)  {
        salaryRepo.delete(one);
        return new GenericResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// upload ///////////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile uploadfile, @RequestParam("oldName") String oldName, @RequestParam("type") String type, final HttpServletRequest request) {

        String dir;
        switch (type) {
            case "CATEGORY.COLLECTION":
                dir = env.getProperty("hanSolo.uploadedFiles.collectionThumbnail");
                break;
            case "SUPPLIERLOGO":
                dir = env.getProperty("hanSolo.uploadedFiles.supplier");
                break;
            case "BANNER":
                dir = env.getProperty("hanSolo.uploadedFiles.banner");
                break;
            case "BLOG":
                dir = env.getProperty("hanSolo.uploadedFiles.blog");
                break;
            case "BLOG.DETAIL":
                dir = env.getProperty("hanSolo.uploadedFiles.blog.detail");
                break;
            case "BIZEXPENSE":
                dir = env.getProperty("hanSolo.uploadedFiles.bizExpense");
                break;
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return Utility.savefile(dir,uploadfile,oldName);
    } // method uploadFile

    @RequestMapping(value = "uploadFiles", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestParam("files") List<MultipartFile> uploadFiles, @RequestParam("oldNames") String oldNames) {

        String directory = env.getProperty("hanSolo.uploadedFiles.product");
        return Utility.saveMultipleFile(directory,uploadFiles, oldNames);
    }
}
