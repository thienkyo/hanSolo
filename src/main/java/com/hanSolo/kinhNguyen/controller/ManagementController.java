package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.*;
import com.hanSolo.kinhNguyen.repository.*;
import com.hanSolo.kinhNguyen.response.*;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
                memberList =  memberRepo.findFirst100ByOrderByGmtModifyDesc();
            }else{
                memberList = memberRepo.findByOrderByGmtModifyDesc();
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
            expAmount += exp.getAmount();
        }
        bizReport.setOutcome(expAmount);

        List<Order> orderList = orderRepo.findByGmtCreateBetween(startDate,endDate);
        int incomeAmount = 0;
        int lensQty = 0;
        int frameQty = 0;
        for(Order or : orderList){
            int amount = 0;
            for(OrderDetail orderDetail : or.getOrderDetails()){
                int lensPrice = orderDetail.getLensPrice() != null ? orderDetail.getLensPrice() : 0;
                amount += orderDetail.getFramePriceAtThatTime() + lensPrice;
                if(orderDetail.getFramePriceAtThatTime() > 1000){
                    frameQty += 1;
                }
                if(orderDetail.getLensPrice() != null && orderDetail.getLensPrice() > 1000){
                    lensQty += 1;
                }
            }
            incomeAmount += amount*(100-or.getCouponDiscount())/100;
        }
        bizReport.setFrameQuantity(frameQty);
        bizReport.setLensQuantity(lensQty);
        bizReport.setIncome(incomeAmount);
        bizReport.setOrderQuantity(orderList.size());

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
        if(amount==100){
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
        if(amount==100){
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

    //////////////////////////// smsUserInfo section /////////////////////////////
    @RequestMapping(value = "getSmsUserInfoForMgnt/{amount}", method = RequestMethod.GET)
    public List<SmsUserInfo> getSmsUserInfoForMgnt(@PathVariable final int amount, final HttpServletRequest request) {
        List<SmsUserInfo> SmsUserInfoList ;
        if(amount==100){
            SmsUserInfoList =  smsUserInfoRepo.findFirst100ByOrderByGmtCreateDesc();
        }else{
            SmsUserInfoList = smsUserInfoRepo.findAllByOrderByGmtCreateDesc();
        }
        return SmsUserInfoList;
    }

    @RequestMapping(value = "upsertSmsUserInfo", method = RequestMethod.POST)
    public SmsUserInfoResponse upsertSmsUserInfo(@RequestBody final SmsUserInfo one, final HttpServletRequest request) {
        SmsUserInfo newOne = smsUserInfoRepo.save(one);
        return new SmsUserInfoResponse(newOne,Utility.SUCCESS_ERRORCODE,"Success");
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
