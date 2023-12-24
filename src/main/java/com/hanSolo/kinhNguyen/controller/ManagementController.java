package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.DTO.ClientShopList;
import com.hanSolo.kinhNguyen.cacheCenter.CommonCache;
import com.hanSolo.kinhNguyen.models.Article;
import com.hanSolo.kinhNguyen.models.Banner;
import com.hanSolo.kinhNguyen.models.BizExpense;
import com.hanSolo.kinhNguyen.models.BizReport;
import com.hanSolo.kinhNguyen.models.Category;
import com.hanSolo.kinhNguyen.models.Client;
import com.hanSolo.kinhNguyen.models.Contract;
import com.hanSolo.kinhNguyen.models.Coupon;
import com.hanSolo.kinhNguyen.models.CustomerSource;
import com.hanSolo.kinhNguyen.models.CustomerSourceReport;
import com.hanSolo.kinhNguyen.models.KeyManagement;
import com.hanSolo.kinhNguyen.models.LensProduct;
import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.models.MemberRole;
import com.hanSolo.kinhNguyen.models.Order;
import com.hanSolo.kinhNguyen.models.OrderDetail;
import com.hanSolo.kinhNguyen.models.Product;
import com.hanSolo.kinhNguyen.models.Salary;
import com.hanSolo.kinhNguyen.models.Shop;
import com.hanSolo.kinhNguyen.models.ShopConfig;
import com.hanSolo.kinhNguyen.models.SmsJob;
import com.hanSolo.kinhNguyen.models.SmsQueue;
import com.hanSolo.kinhNguyen.models.SmsUserInfo;
import com.hanSolo.kinhNguyen.models.SpecificSmsUserInfo;
import com.hanSolo.kinhNguyen.models.Strategy;
import com.hanSolo.kinhNguyen.models.Supplier;
import com.hanSolo.kinhNguyen.models.UsedCoupons;
import com.hanSolo.kinhNguyen.repository.ArticleRepository;
import com.hanSolo.kinhNguyen.repository.BannerRepository;
import com.hanSolo.kinhNguyen.repository.BizExpenseRepository;
import com.hanSolo.kinhNguyen.repository.BizReportRepository;
import com.hanSolo.kinhNguyen.repository.CategoryRepository;
import com.hanSolo.kinhNguyen.repository.ClientRepository;
import com.hanSolo.kinhNguyen.repository.ContractRepository;
import com.hanSolo.kinhNguyen.repository.CouponRepository;
import com.hanSolo.kinhNguyen.repository.CustomerSourceReportRepository;
import com.hanSolo.kinhNguyen.repository.CustomerSourceRepository;
import com.hanSolo.kinhNguyen.repository.KeyManagementRepository;
import com.hanSolo.kinhNguyen.repository.LensProductRepository;
import com.hanSolo.kinhNguyen.repository.MemberRepository;
import com.hanSolo.kinhNguyen.repository.MemberRoleRepository;
import com.hanSolo.kinhNguyen.repository.OrderDetailRepository;
import com.hanSolo.kinhNguyen.repository.OrderRepository;
import com.hanSolo.kinhNguyen.repository.ProductRepository;
import com.hanSolo.kinhNguyen.repository.SalaryRepository;
import com.hanSolo.kinhNguyen.repository.ShopConfigRepository;
import com.hanSolo.kinhNguyen.repository.ShopRepository;
import com.hanSolo.kinhNguyen.repository.SmsJobRepository;
import com.hanSolo.kinhNguyen.repository.SmsQueueRepository;
import com.hanSolo.kinhNguyen.repository.SmsUserInfoRepository;
import com.hanSolo.kinhNguyen.repository.SpecificSmsUserInfoRepository;
import com.hanSolo.kinhNguyen.repository.StrategyRepository;
import com.hanSolo.kinhNguyen.repository.SupplierRepository;
import com.hanSolo.kinhNguyen.repository.UsedCouponsRepository;
import com.hanSolo.kinhNguyen.request.QueryByClientShopAmountRequest;
import com.hanSolo.kinhNguyen.response.BizReportResponse;
import com.hanSolo.kinhNguyen.response.CategoryResponse;
import com.hanSolo.kinhNguyen.response.CouponResponse;
import com.hanSolo.kinhNguyen.response.CustomerSourceResponse;
import com.hanSolo.kinhNguyen.response.GeneralResponse;
import com.hanSolo.kinhNguyen.response.GenericResponse;
import com.hanSolo.kinhNguyen.response.KeyManagementResponse;
import com.hanSolo.kinhNguyen.response.SmsJobResponse;
import com.hanSolo.kinhNguyen.response.SmsQueueResponse;
import com.hanSolo.kinhNguyen.response.SmsUserInfoResponse;
import com.hanSolo.kinhNguyen.response.SpecificSmsUserInfoResponse;
import com.hanSolo.kinhNguyen.response.SupplierResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    @Autowired private LensProductRepository lensProductRepo;
    @Autowired private StrategyRepository strategyRepo;
    @Autowired private ShopConfigRepository shopConfigRepo;
    @Autowired private MemberRoleRepository memberRoleRepo;
    @Autowired private ShopRepository shopRepo;
    @Autowired private ClientRepository clientRepo;

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
    @RequestMapping(value = "getBizExpenseForMgnt", method = RequestMethod.POST)
    public List<BizExpense> getBizExpenseForMgnt(@RequestBody final QueryByClientShopAmountRequest req, final HttpServletRequest request) {
        List<BizExpense> bizExpenseList;
        if(req.getAmount() == Utility.FIRTST_TIME_LOAD_SIZE){
            if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE)
                    && (req.getClientCode().equalsIgnoreCase("ALL") || req.getClientCode().equalsIgnoreCase(Utility.GODLIKE_ROLE)) ){
                bizExpenseList =  bizExpenseRepo.findFirst100ByOrderByGmtCreateDesc();
            }else if(req.getShopCode().equalsIgnoreCase("ALL")){
                bizExpenseList =  bizExpenseRepo.findFirst100ByClientCodeOrderByGmtCreateDesc(req.getClientCode());
            }else{
                bizExpenseList =  bizExpenseRepo.findFirst100ByClientCodeAndShopCodeOrderByGmtCreateDesc(req.getClientCode(),req.getShopCode());
            }
        }else{
            if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE)
                    && (req.getClientCode().equalsIgnoreCase("ALL") || (req.getClientCode().equalsIgnoreCase(Utility.GODLIKE_ROLE)) ) ){
                bizExpenseList =  bizExpenseRepo.findAllByOrderByGmtCreateDesc();
            }else if(req.getShopCode().equalsIgnoreCase("ALL")){
                bizExpenseList =  bizExpenseRepo.findByClientCodeOrderByGmtCreateDesc(req.getClientCode());
            }else{
                bizExpenseList =  bizExpenseRepo.findByClientCodeAndShopCodeOrderByGmtCreateDesc(req.getClientCode(),req.getShopCode());
            }
        }

        return bizExpenseList;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "upsertBizExpense", method = RequestMethod.POST)
    public GeneralResponse<BizExpense> upsertBizExpense(@RequestBody final BizExpense bizExpense, final HttpServletRequest request) throws ParseException {
        bizExpense.setGmtModify(Utility.getCurrentDate());
        return new GeneralResponse(bizExpenseRepo.save(bizExpense),Utility.SUCCESS_ERRORCODE,"Success");
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "deleteBizExpense", method = RequestMethod.POST)
    public GeneralResponse deleteBizExpense(@RequestBody final BizExpense bizExpense, final HttpServletRequest request)  {
        if(!onlyAllowThisRole(request,Utility.ADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        bizExpenseRepo.delete(bizExpense);
        return new GeneralResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "updateBizExpenseStatus", method = RequestMethod.POST)
    public GeneralResponse updateBizExpenseStatus(@RequestBody final BizExpense bizExpense, final HttpServletRequest request) throws ParseException {
        if(!onlyAllowThisRole(request,Utility.SUPERADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        bizExpenseRepo.updateStatusAndGmtModifyById(bizExpense.getStatus(),Utility.getCurrentDate(),bizExpense.getId());

        return new GeneralResponse("upsert_bizExpense_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////////Member section/////////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getMemberForMgnt/{amount}", method = RequestMethod.GET)
    public List<Member> getMemberForMgnt(@PathVariable final int amount) {

        List<Member> memberList;
        if(amount==Utility.FIRTST_TIME_LOAD_SIZE){
            memberList =  memberRepo.findFirst100ByOrderByGmtCreateDesc();
        }else{
            memberList = memberRepo.findByOrderByGmtCreateDesc();
        }

       /* List<Member> godLike = memberRepo.findByMemberRoles_Role("GODLIKE");
        memberList.removeAll(godLike);*/

        MemberRole mr = new MemberRole();
        mr.setRole(Utility.GODLIKE_ROLE);
        memberList.removeIf(i -> i.getMemberRoles().contains(mr));
        memberList.forEach(item -> item.setPass(""));

        return memberList;
    }

    @RequestMapping(value = "getMemberByClientCode/{amount}", method = RequestMethod.GET)
    public List<Member> getMemberByClientCode(@PathVariable final int amount,final HttpServletRequest request) {
        final Claims claims = (Claims) request.getAttribute("claims");
        Map<String,String> clientInfo = (Map<String, String>) claims.get("clientInfo");

        List<Member> memberList;
        if(amount==Utility.FIRTST_TIME_LOAD_SIZE){
            memberList =  memberRepo.findFirst100ByClientCodeOrderByGmtCreateDesc(clientInfo.get("clientCode"));
        }else{
            memberList = memberRepo.findByClientCodeOrderByGmtCreateDesc(clientInfo.get("clientCode"));
        }
        memberList.forEach(item -> item.setPass(""));

        return memberList;
    }

    @RequestMapping(value = "getMemberByTerms", method = RequestMethod.POST)
    public List<Member> getMemberByTerms(@RequestBody final QueryByClientShopAmountRequest req,final HttpServletRequest request) {
        List<Member> memberList = null;
        if(req.getAmount() == Utility.FIRTST_TIME_LOAD_SIZE){
            if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE)
                    && (req.getClientCode().equalsIgnoreCase("ALL")) || (req.getClientCode().equalsIgnoreCase(Utility.GODLIKE_ROLE)) ){
                memberList =  memberRepo.findFirst100ByOrderByGmtCreateDesc();
            }else if(req.getShopCode().equalsIgnoreCase("ALL")){
                memberList =  memberRepo.findFirst100ByClientCodeOrderByGmtCreateDesc(req.getClientCode());
            }else{
                memberList =  memberRepo.findFirst100ByClientCodeAndShopCodeOrderByGmtCreateDesc(req.getClientCode(),req.getShopCode());
            }
        }else{
            if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE) && req.getClientCode().equalsIgnoreCase("ALL")){
                memberList =  memberRepo.findAllByOrderByGmtCreateDesc();
            }else if(req.getShopCode().equalsIgnoreCase("ALL")){
                memberList =  memberRepo.findByClientCodeOrderByGmtCreateDesc(req.getClientCode());
            }else{
                memberList =  memberRepo.findByClientCodeAndShopCodeOrderByGmtCreateDesc(req.getClientCode(),req.getShopCode());
            }
        }
        memberList.forEach(item -> item.setPass(""));
        return memberList;

    }

    @RequestMapping(value = "upsertMember", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GeneralResponse<String> upsertMember(@RequestBody final Member member,final HttpServletRequest request)
            throws ParseException {

        if(!onlyAllowThisRole(request,Utility.SUPERADMIN_ROLE) ){
            return new GeneralResponse("mgnt/upsertMember: no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }


        Optional<Member>  memberOpt = memberRepo.findById(member.getId());
        if(memberOpt.isPresent()){
            member.setPass(memberOpt.get().getPass());
            member.setGmtModify(Utility.getCurrentDate());
        }
        memberRepo.save(member);

        return new GeneralResponse("upsert_member_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "updateMemberStatus", method = RequestMethod.POST)
    public GeneralResponse<String> updateMemberStatus(@RequestBody final Member mem) throws ParseException {
        memberRepo.updateStatusAndGmtModifyById(mem.getStatus(),Utility.getCurrentDate(),mem.getId());

        if(CommonCache.LOGIN_MEMBER_LIST.containsKey(mem.getPhone())){
            CommonCache.LOGIN_MEMBER_LIST.remove(mem.getPhone());
        }

        return new GeneralResponse("upsert_member_success",Utility.SUCCESS_ERRORCODE,Utility.FAIL_MSG);
    }

    @RequestMapping(value = "upsertMemberByAdmin", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GeneralResponse<Member> upsertMemberByAdmin(@RequestBody final Member member,final HttpServletRequest request)
            throws ParseException {

        if(!onlyAllowThisRole(request,Utility.SUPERADMIN_ROLE) ){
            return new GeneralResponse("mgnt/upsertMemberByAdmin: no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }

        Date now = Utility.getCurrentDate();
        Optional<Member>  memberOpt = memberRepo.findByPhone(member.getPhone());
        if(!memberOpt.isPresent() && member.getId() == 0){
            List<MemberRole> roleList = new ArrayList<>();
            member.setPass(Utility.DEFAULT_PW);
            member.setGmtCreate(now);
            member.setGmtModify(now);
            member.setStatus(Utility.INACTIVE_STATUS);
            roleList.add(new MemberRole(Utility.MEMBER_ROLE, "0", member.getFullName(), member.getPhone(), member, now, now));
            member.setMemberRoles(roleList);

            return new GeneralResponse(memberRepo.save(member),Utility.SUCCESS_ERRORCODE,Utility.INSERT_SU_MSG);
        }else{
            Member mb = memberOpt.get();
            mb.setFullName(member.getFullName());
            mb.setPhone(member.getPhone());
            mb.setAddress(member.getAddress());
            mb.setEmail(member.getEmail());
            mb.setClientCode(member.getClientCode());
            mb.setShopCode(member.getShopCode());
            mb.setGmtModify(now);

            return new GeneralResponse(memberRepo.save(mb),Utility.SUCCESS_ERRORCODE,Utility.UPDATE_SU_MSG);
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public GeneralResponse<String> logout( final HttpServletRequest request) {
        final Claims claims = (Claims) request.getAttribute("claims");
        CommonCache.LOGIN_MEMBER_LIST.remove(claims.get("sub")+"");
        return new GeneralResponse("logout success",Utility.SUCCESS_ERRORCODE,"logout success");
    }

    //////////////////////////////Member Role section/////////////////////////////
    /*@RequestMapping(value = "upsertMemberRole", method = RequestMethod.POST)
    public GeneralResponse<MemberRole> upsertMemberRole(@RequestBody final MemberRole role, final HttpServletRequest request)
            throws ServletException, ParseException {
        if(role.getId() == 0){
            role.setGmtCreate(Utility.getCurrentDate());
        }
        role.setGmtModify(Utility.getCurrentDate());
        return new GeneralResponse(memberRoleRepo.save(role),Utility.SUCCESS_ERRORCODE,Utility.SUCCESS_MSG);
    }*/

    @RequestMapping(value = "deleteMemberRole", method = RequestMethod.POST)
    public GeneralResponse<String> deleteMemberRole(@RequestBody final MemberRole role, final HttpServletRequest request) throws ServletException {
        if(!onlyAllowThisRole(request,Utility.SUPERADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        memberRoleRepo.delete(role);
        return new GeneralResponse("delete_memberRole_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////////client shop section/////////////////////////////
    // for not GODLIKE
    @RequestMapping(value = "getClientShopList", method = RequestMethod.GET)
    public GeneralResponse<ClientShopList> getClientShopList(final HttpServletRequest request) {
        final Claims claims = (Claims) request.getAttribute("claims");
        String shopCode = (String) claims.get("shopCode");
        Map<String,String> clientInfo = (Map<String, String>) claims.get("clientInfo");

        ClientShopList csl = new ClientShopList();
        List<Shop> shopList;
        List<Shop> clientShopList = shopRepo.findByClientCodeOrderByGmtCreateDesc(clientInfo.get("clientCode"));

        List<Client> clientList =  clientRepo.findByClientCode(clientInfo.get("clientCode"));
        if(shopCode.equals("ALL")){
            shopList = clientShopList;
            if(CommonCache.CLIENT_SHOP_LIST.size() == Utility.LOGIN_MEMBER_LIST_SIZE){
                CommonCache.CLIENT_SHOP_LIST.clear();
            }
            CommonCache.CLIENT_SHOP_LIST.put(clientInfo.get("clientCode"),shopList);
        }else{
            shopList = shopRepo.findByClientCodeAndShopCode(clientInfo.get("clientCode"), shopCode);
        }

        csl.setClientList(clientList);
        csl.setShopList(shopList);
        csl.setOneClientShopList(clientShopList);

        return new GeneralResponse(csl,Utility.SUCCESS_ERRORCODE,"success");
    }

    //////////////////////////// Coupon section ///////////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getAllCoupons", method = RequestMethod.GET)
    public List<Coupon> getAllCoupons(final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");
        Map<String,String> clientInfo = (Map<String, String>) claims.get("clientInfo");

        if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE)){
            return couponRepo.findAllByOrderByGmtCreateDesc();
        }
        return couponRepo.findByClientCodeOrderByGmtCreateDesc(clientInfo.get("clientCode"));
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
    public GeneralResponse deleteCoupon(@RequestBody final Coupon coupon, final HttpServletRequest request) throws ServletException {
        if(!onlyAllowThisRole(request,Utility.ADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        couponRepo.delete(coupon);
        return new GeneralResponse("delete_coupon_success",Utility.SUCCESS_ERRORCODE,"Success");
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


    @RequestMapping(value = "getBizReportByCondition", method = RequestMethod.POST)
    public List<BizReport> getBizReportByCondition(@RequestBody final QueryByClientShopAmountRequest req,final HttpServletRequest request) {
        List<BizReport> bizReportList = new ArrayList<>();
        if(onlyAllowThisRole(request,Utility.SUPERADMIN_ROLE)){
            if(req.getShopCode().equalsIgnoreCase("ALL")){
                bizReportList =  bizReportRepo.findByClientCodeOrderByYearDescMonthDesc(req.getClientCode());
            }else{
                bizReportList =  bizReportRepo.findByClientCodeAndShopCodeOrderByYearDescMonthDesc(req.getClientCode(),req.getShopCode());
            }
        }
        return bizReportList;
    }


    @RequestMapping(value = "upsertBizReport", method = RequestMethod.POST)
    public GeneralResponse upsertBizReport(@RequestBody final BizReport bizReport, final HttpServletRequest request) throws ParseException {
        if(!Utility.onlyAllowThisRole(request,Utility.ADMIN_ROLE) ){
            return new GeneralResponse(null,Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }

        if(bizReport.getId() != 0){
            return new GeneralResponse(null,Utility.FAIL_ERRORCODE,"fail");
        }

        List<Shop> shopList = shopRepo.findByClientCodeOrderByGmtCreateDesc(bizReport.getClientCode());
        List<BizReport> reportList = new ArrayList<>();
        for(Shop shop : shopList){
            BizReport newReport = new BizReport();
            newReport.setYear(bizReport.getYear());
            newReport.setMonth(bizReport.getMonth());
            newReport.setClientCode(bizReport.getClientCode());
            newReport.setShopCode(shop.getShopCode());
            newReport.setGmtCreate(Utility.getCurrentDate());
            newReport.setGmtModify(Utility.getCurrentDate());

            reportList.add(newReport);
        }
        reportList = (List<BizReport>) bizReportRepo.saveAll(reportList);

        BizReport report1 = reportList.stream()
                .filter(rp -> bizReport.getShopCode().equals(rp.getShopCode()))
                .findAny()
                .orElse(null);

        return new GeneralResponse(report1,Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "calculateReport", method = RequestMethod.POST)
    public GeneralResponse calculateReport(@RequestBody final BizReport bizReport, final HttpServletRequest request) throws ParseException {
        if(!Utility.onlyAllowThisRole(request,Utility.ADMIN_ROLE) ){
            return new GeneralResponse(null,Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        bizReport.setGmtModify(Utility.getCurrentDate());
        Date startDate = Utility.getFirstDateOfMonth(bizReport.getYear(),bizReport.getMonth());
        Date endDate = Utility.getLastDateOfMonth(bizReport.getYear(),bizReport.getMonth());

        // get biz expense
        List<BizExpense> expenses = bizExpenseRepo.findByClientCodeAndShopCodeAndGmtCreateBetween(
                bizReport.getClientCode(), bizReport.getShopCode(), startDate,endDate);
        int expAmount = 0;
        for(BizExpense exp : expenses){
            expAmount += exp.getAmount() != null ? exp.getAmount() : 0;
        }
        // get salary
        List<Salary>  salaries = salaryRepo.findByYearAndMonthAndClientCodeAndShopCode(bizReport.getYear(),bizReport.getMonth(),
                bizReport.getClientCode(), bizReport.getShopCode());
        for(Salary sal : salaries){
            expAmount += (sal.getAmount() != null ? sal.getAmount() : 0 ) + (sal.getBonus() != null ? sal.getBonus() : 0);
        }
        bizReport.setOutcome(expAmount);

        // get order from client.shop in a month
        List<Order> orderList = orderRepo.findByClientCodeAndShopCodeAndGmtCreateBetween(bizReport.getClientCode(),
                bizReport.getShopCode(),startDate,endDate);
        int incomeAmount = 0;
        int discountAmount = 0;
        int lensQty = 0;
        int frameQty = 0;
        int orderQty = 0;
        for(Order or : orderList){
            int amount = 0;// one order amount
            int disAmount = 0;//
            int tempDisAmount ;//
            for(OrderDetail orderDetail : or.getOrderDetails()){
                int lensPrice = orderDetail.getLensPrice() != null ? orderDetail.getLensPrice() : 0;
                int otherPrice = orderDetail.getOtherPrice() != null ? orderDetail.getOtherPrice() : 0;

                //calculate each orderDetail discount value
                tempDisAmount = orderDetail.getFramePriceAtThatTime()*orderDetail.getFrameDiscountAmount()*orderDetail.getQuantity()/100
                             + lensPrice*orderDetail.getLensDiscountAmount()*orderDetail.getLensQuantity()/100;
                disAmount += tempDisAmount;
                //calculate each orderDetail amount
                amount += orderDetail.getFramePriceAtThatTime()*orderDetail.getQuantity() + lensPrice*orderDetail.getLensQuantity() +
                        otherPrice - tempDisAmount;

                if(orderDetail.getFramePriceAtThatTime() > 1000){
                    frameQty += orderDetail.getQuantity();
                }
                if(orderDetail.getLensPrice() != null && orderDetail.getLensPrice() > 1000){
                    lensQty += orderDetail.getLensQuantity();
                }
            }
            //total discount in one order
            discountAmount += disAmount + amount*or.getCouponDiscount()/100 + or.getCustomDiscountAmount();
            //total amount in one order
            incomeAmount += amount - amount*or.getCouponDiscount()/100 - or.getCustomDiscountAmount();

            if(amount > 10000){
                orderQty += 1;
            }
        }
        bizReport.setFrameQuantity(frameQty);
        bizReport.setLensQuantity(lensQty);
        bizReport.setIncome(incomeAmount);
        bizReport.setOrderQuantity(orderQty);
        bizReport.setDiscountAmount(discountAmount);

        return new GeneralResponse(bizReportRepo.save(bizReport),Utility.SUCCESS_ERRORCODE,Utility.SUCCESS_MSG);
    }

    @RequestMapping(value = "deleteBizReport", method = RequestMethod.POST)
    public GeneralResponse deleteBizReport(@RequestBody final BizReport bizReport,final HttpServletRequest request) {
        if(!onlyAllowThisRole(request,Utility.ADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        bizReportRepo.delete(bizReport);
        return new GeneralResponse("delete_bizReport_success",Utility.SUCCESS_ERRORCODE,"Success");
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

    /////////////////////////Orders section ///////////////////////
    @SuppressWarnings("unchecked")
    @Deprecated
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

    ////
    @RequestMapping(value = "getOrdersByTerms", method = RequestMethod.POST)
    public List<Order> getOrdersByTerms(@RequestBody final QueryByClientShopAmountRequest req, final HttpServletRequest request) {
        List<Order> orderList = null;
        if(req.getAmount() == Utility.FIRTST_TIME_LOAD_SIZE){
            if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE)
                    && (req.getClientCode().equalsIgnoreCase("ALL") || req.getClientCode().equalsIgnoreCase(Utility.GODLIKE_ROLE)) ){
                orderList =  orderRepo.findFirst100ByOrderByGmtCreateDesc();
            }else if(req.getShopCode().equalsIgnoreCase("ALL")){
                orderList =  orderRepo.findFirst100ByClientCodeOrderByGmtCreateDesc(req.getClientCode());
            }else{
                orderList =  orderRepo.findFirst100ByClientCodeAndShopCodeOrderByGmtCreateDesc(req.getClientCode(),req.getShopCode());
            }
        }else{
            if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE)
                    && (req.getClientCode().equalsIgnoreCase("ALL") || (req.getClientCode().equalsIgnoreCase(Utility.GODLIKE_ROLE)) ) ){
                orderList =  orderRepo.findAllByOrderByGmtCreateDesc();
            }else if(req.getShopCode().equalsIgnoreCase("ALL")){
                orderList =  orderRepo.findByClientCodeOrderByGmtCreateDesc(req.getClientCode());
            }else{
                orderList =  orderRepo.findByClientCodeAndShopCodeOrderByGmtCreateDesc(req.getClientCode(),req.getShopCode());
            }
        }
        return orderList;
    }

    @RequestMapping(value = "updateOrderStatus", method = RequestMethod.POST)
    public GeneralResponse<String> updateOrder(@RequestBody final Order order, final HttpServletRequest request) throws ParseException {
        if(checkSameClientCode(request, order.getClientCode()) || onlyAllowThisRole(request,Utility.GODLIKE_ROLE)){
            orderRepo.updateStatusAndGmtModifyById(order.getStatus(),Utility.getCurrentDate(),order.getId());
            return new GeneralResponse("upsert_order_success",Utility.SUCCESS_ERRORCODE,"Success");
        }
        return new GeneralResponse("upsert_order_fail",Utility.FAIL_ERRORCODE,"Error ot not allow");

    }

    @RequestMapping(value = "getOrderById", method = RequestMethod.POST)
    public GeneralResponse<Order> getOrderById(@RequestBody final QueryByClientShopAmountRequest req,final HttpServletRequest request) throws ServletException {

        Optional<Order> orOpt;
        if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE) ){
            orOpt = orderRepo.findById(Integer.parseInt(req.getGeneralPurpose()));
        }else{
            orOpt = orderRepo.findByIdAndClientCode(Integer.parseInt(req.getGeneralPurpose()),req.getClientCode());
        }
        if(orOpt.isPresent()){
            if(CommonCache.ORDER_LIST.size() == Utility.ORDER_LIST_SIZE){
                CommonCache.ORDER_LIST.clear();
            }
            CommonCache.ORDER_LIST.put(orOpt.get().getId(),orOpt.get());
            return new GeneralResponse(orOpt.get(),Utility.SUCCESS_ERRORCODE,"Success");
        }

        return new GeneralResponse(null,Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
    }

    /**
     * for printable
     * @param orderId
     * @param request
     * @return
     * @throws ServletException
     */
    @RequestMapping(value = "getOrderById/{orderId}", method = RequestMethod.GET)
    public GeneralResponse<Order> getOrderById(@PathVariable final int orderId,final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");
        Map<String,String> clientInfo = (Map<String, String>) claims.get("clientInfo");

        Optional<Order> orOpt;
        if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE) ){
            orOpt = orderRepo.findById(orderId);
        }else{
            orOpt = orderRepo.findByIdAndClientCode(orderId,clientInfo.get("clientCode"));
        }

        if(orOpt.isPresent()){
            return new GeneralResponse(orOpt.get(),Utility.SUCCESS_ERRORCODE,"Success");
        }
        return new GeneralResponse(null,Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
    }

    @RequestMapping(value = "deleteOrder", method = RequestMethod.POST)
    public GeneralResponse<String> deleteOrder(@RequestBody final Order order, final HttpServletRequest request) {
        if(checkSameClientCode(request, order.getClientCode()) || onlyAllowThisRole(request,Utility.GODLIKE_ROLE)){
            orderRepo.delete(order);
            return new GeneralResponse("delete_order_success",Utility.SUCCESS_ERRORCODE,Utility.SUCCESS_MSG);
        }
        return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
    }

    @RequestMapping(value = "deleteOrderDetail", method = RequestMethod.POST)
    public GeneralResponse<String> deleteOrderDetail(@RequestBody final OrderDetail orderDetail, final HttpServletRequest request) {
        if(checkSameClientCode(request, orderDetail.getClientCode()) || onlyAllowThisRole(request,Utility.GODLIKE_ROLE)){
            orderDetailRepo.delete(orderDetail);
            return new GeneralResponse("delete_order_success",Utility.SUCCESS_ERRORCODE,Utility.SUCCESS_MSG);
        }
        return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
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
    public GeneralResponse<String> updateCusSource(@RequestBody final Order order, final HttpServletRequest request) throws ParseException {
        if(checkSameClientCode(request, order.getClientCode()) || onlyAllowThisRole(request,Utility.GODLIKE_ROLE)){
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
            return new GeneralResponse("upsert_order_success",Utility.SUCCESS_ERRORCODE,"Success");
        }

        return new GeneralResponse("upsert_order_fail",Utility.FAIL_ERRORCODE,"not allow");
    }

    @RequestMapping(value = "saveMultipleOrders", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse saveMultipleOrders(@RequestBody final List<Order> orders, final HttpServletRequest request) throws ServletException, ParseException {
        orderRepo.saveAll(orders);
        GenericResponse response =  new GenericResponse("Success",Utility.SUCCESS_ERRORCODE,"save order success");
        return response;
    }

    @RequestMapping(value = "recoverOrder", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GeneralResponse<List<Order>> recoverOrder(@RequestBody final List<Order> orders, final HttpServletRequest request) throws ServletException, ParseException {

        GeneralResponse response =  new GeneralResponse(orderRepo.saveAll(orders),Utility.SUCCESS_ERRORCODE,"save order success");
        return response;
    }

    @RequestMapping(value = "getOrderHistory", method = RequestMethod.POST)
    public List<OrderDetail> getOrderHistory(@RequestBody final QueryByClientShopAmountRequest req, final HttpServletRequest request) {
        List<Order> orderList;
        List<OrderDetail> orderDetailList;

        final Claims claims = (Claims) request.getAttribute("claims");
        Map<String,String> clientInfo = (Map<String, String>) claims.get("clientInfo");

        if(Utility.onlyAllowThisRole(request,Utility.GODLIKE_ROLE)){
            orderList = orderRepo.findFirst40ByShippingPhoneContainsOrderByGmtCreateDesc(req.getGeneralPurpose());
            orderDetailList = orderDetailRepo.findFirst40ByPhoneContainsOrderByGmtCreateDesc(req.getGeneralPurpose());
        }else{
            orderList = orderRepo.findFirst40ByClientCodeAndShippingPhoneOrderByGmtCreateDesc(clientInfo.get("clientCode"), req.getGeneralPurpose());
            orderDetailList = orderDetailRepo.findFirst40ByClientCodeAndPhoneOrderByGmtCreateDesc(clientInfo.get("clientCode"), req.getGeneralPurpose());
        }

        List<OrderDetail> result = new ArrayList<>();

        if(!orderList.isEmpty()){
            for(Order or : orderList){
                result.addAll(or.getOrderDetails());
            }
        }

        if(!orderDetailList.isEmpty()){
            for(OrderDetail od : orderDetailList){
                boolean flag = true;
                for(OrderDetail r : result){
                    if(od.getPhone().equals(r.getPhone())){
                        flag = false;
                    }
                }
                if(flag){
                    result.add(od);
                }
            }
        }

        return	result;
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
    public GeneralResponse deleteSmsUserInfo(@RequestBody final SmsUserInfo one, final HttpServletRequest request)  {
        if(!onlyAllowThisRole(request,Utility.ADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        smsUserInfoRepo.delete(one);
        return new GeneralResponse("",Utility.SUCCESS_ERRORCODE,"Success");
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
    public GeneralResponse deleteSmsQueue(@RequestBody final SmsQueue one, final HttpServletRequest request)  {
        if(!onlyAllowThisRole(request,Utility.ADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        smsQueueRepo.delete(one);
        return new GeneralResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "delete100SmsQueue", method = RequestMethod.POST)
    public GeneralResponse delete100SmsQueue(final HttpServletRequest request)  {
        if(!onlyAllowThisRole(request,Utility.ADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }

        List<SmsQueue> smsQueueList = smsQueueRepo.findFirst100ByStatusOrderByGmtCreateAsc(Utility.SMS_QUEUE_SENT);
        if(!smsQueueList.isEmpty()){
            smsQueueRepo.deleteAll(smsQueueList);
        }
        return new GeneralResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// sms Job section /////////////////////////////

    @RequestMapping(value = "togglePrepareSmsData", method = RequestMethod.POST)
    public boolean togglePrepareSmsData()  {
        CommonCache.SMS_DATA_PREPARE_CONTROL = CommonCache.SMS_DATA_PREPARE_CONTROL ? false : true;
        return CommonCache.SMS_DATA_PREPARE_CONTROL;
    }

    @RequestMapping(value = "getSmsDataPrepareStatus", method = RequestMethod.POST)
    public boolean getSmsDataPrepareStatus()  {
        return CommonCache.SMS_DATA_PREPARE_CONTROL;
    }

    @RequestMapping(value = "toggleSmsSend", method = RequestMethod.POST)
    public boolean toggleSmsSend()  {
        CommonCache.SMS_SEND_CONTROL = CommonCache.SMS_SEND_CONTROL ? false : true;
        CommonCache.LAST_SENT_SMS = null;
        return CommonCache.SMS_SEND_CONTROL;
    }

    @RequestMapping(value = "getSmsSendStatus", method = RequestMethod.POST)
    public boolean getSmsSendStatus()  {
        return CommonCache.SMS_SEND_CONTROL;
    }


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
    public GeneralResponse deleteSmsJob(@RequestBody final SmsJob one, final HttpServletRequest request)  {
        if(!onlyAllowThisRole(request,Utility.ADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        smsJobRepo.delete(one);
        return new GeneralResponse("delete_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "getLastHeartBeatTime", method = RequestMethod.POST)
    public Date getSMSLastTrigger()  {
        return CommonCache.LAST_SMS_HEARTBEAT_TIME;
    }

    @RequestMapping(value = "getLastPrepareDataTime", method = RequestMethod.POST)
    public Date getPrepareDataLastTrigger()  {
        return CommonCache.LAST_PREPARE_DATA_HEARTBEAT_TIME;
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
    public GeneralResponse deleteSpecificSmsUserInfo(@RequestBody final SpecificSmsUserInfo one, final HttpServletRequest request)  {
        if(!onlyAllowThisRole(request,Utility.ADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        specificSmsUserInfoRepo.delete(one);
        return new GeneralResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// strategy section /////////////////////////////
    @RequestMapping(value = "getStrategyForMgnt/{amount}", method = RequestMethod.GET)
    public List<Strategy> getStrategyForMgnt(@PathVariable final int amount, final HttpServletRequest request) {
        List<Strategy> list ;
        if(amount == Utility.FIRTST_TIME_LOAD_SIZE){
            list =  strategyRepo.findFirst100ByOrderByGmtCreateDesc();
        }else{
            list = strategyRepo.findAllByOrderByGmtCreateDesc();
        }
        return list;
    }

    @RequestMapping(value = "upsertStrategy", method = RequestMethod.POST)
    public GeneralResponse<Strategy> upsertStrategy(@RequestBody final Strategy one) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
        }
        one.setGmtModify(Utility.getCurrentDate());
        return new GeneralResponse(strategyRepo.save(one),Utility.SUCCESS_ERRORCODE,"Save strategy success");
    }

    @RequestMapping(value = "deleteStrategy", method = RequestMethod.POST)
    public GenericResponse deleteStrategy(@RequestBody final Strategy one)  {
        strategyRepo.delete(one);
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
   /* @RequestMapping(value = "getAllContract", method = RequestMethod.GET)
    public List<Contract> getAllContract() {
        return contractRepo.findAllByOrderByGmtCreateDesc();
    }
*/

    @RequestMapping(value = "getContractByCondition", method = RequestMethod.POST)
    public List<Contract> getContractByCondition(@RequestBody final QueryByClientShopAmountRequest req,final HttpServletRequest request) {
        List<Contract> contractList = null;
        if(req.getAmount() == Utility.FIRTST_TIME_LOAD_SIZE){
            if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE)
                    && (req.getClientCode().equalsIgnoreCase("ALL")) || (req.getClientCode().equalsIgnoreCase(Utility.GODLIKE_ROLE)) ){
                contractList =  contractRepo.findFirst100ByOrderByGmtCreateDesc();
            }else if(req.getShopCode().equalsIgnoreCase("ALL")){
                contractList =  contractRepo.findFirst100ByClientCodeOrderByGmtCreateDesc(req.getClientCode());
            }else{
                contractList =  contractRepo.findFirst100ByClientCodeAndShopCodeOrderByGmtCreateDesc(req.getClientCode(),req.getShopCode());
            }
        }else{
            if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE) && req.getClientCode().equalsIgnoreCase("ALL")){
                contractList =  contractRepo.findAllByOrderByGmtCreateDesc();
            }else if(req.getShopCode().equalsIgnoreCase("ALL")){
                contractList =  contractRepo.findByClientCodeOrderByGmtCreateDesc(req.getClientCode());
            }else{
                contractList =  contractRepo.findByClientCodeAndShopCodeOrderByGmtCreateDesc(req.getClientCode(),req.getShopCode());
            }
        }

        return contractList;

    }




    @RequestMapping(value = "upsertContract", method = RequestMethod.POST)
    public GeneralResponse<Contract> upsertSalary(@RequestBody final Contract one) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
        }
        one.setGmtModify(Utility.getCurrentDate());
        return new GeneralResponse(contractRepo.save(one),Utility.SUCCESS_ERRORCODE,"Save key success");
    }

    @RequestMapping(value = "deleteContract", method = RequestMethod.POST)
    public GeneralResponse deleteContract(@RequestBody final Contract one, final HttpServletRequest request)  {
        if(!onlyAllowThisRole(request,Utility.SUPERADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        contractRepo.delete(one);
        return new GeneralResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// salary section /////////////////////////////
    @RequestMapping(value = "getAllSalaryOnePerson", method = RequestMethod.GET)
    public List<Salary> getAllSalaryOnePerson() {
        return salaryRepo.findAllByOrderByGmtCreateDesc();
    }


    @RequestMapping(value = "getAllSalaryOnePerson/{contractId}", method = RequestMethod.GET)
    public List<Salary> getAllSalaryOnePerson(@PathVariable final int contractId, final HttpServletRequest request) {

        List<Salary> salaryList = new ArrayList<>();
        if(onlyAllowThisRole(request,Utility.SUPERADMIN_ROLE) ){
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
    public GeneralResponse deleteSalary(@RequestBody final Salary one, final HttpServletRequest request)  {
        if(!onlyAllowThisRole(request,Utility.SUPERADMIN_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        salaryRepo.delete(one);
        return new GeneralResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// shop config section /////////////////////////////
    @RequestMapping(value = "getShopConfig", method = RequestMethod.GET)
    @Cacheable("shopInfo")
    public GeneralResponse<ShopConfig> getShopConfig() {
        return new GeneralResponse(shopConfigRepo.findFirstByOrderByIdAsc(),Utility.SUCCESS_ERRORCODE,"Save key success");
    }

    @RequestMapping(value = "refreshShopConfig", method = RequestMethod.GET)
    @CacheEvict(value = "shopInfo",allEntries = true)
    public GeneralResponse<ShopConfig> refreshShopConfig() {
        return new GeneralResponse("refresh success",Utility.SUCCESS_ERRORCODE,"success");
    }

    @RequestMapping(value = "upsertShopConfig", method = RequestMethod.POST)
    public GeneralResponse<ShopConfig> upsertShopConfig(@RequestBody final ShopConfig one) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
        }
        one.setGmtModify(Utility.getCurrentDate());
        return new GeneralResponse(shopConfigRepo.save(one),Utility.SUCCESS_ERRORCODE,"Save key success");
    }


    //////////////////////////// search section///////////////////////////////
    /*@RequestMapping("searchLensProduct/{keySearch}")
    public List<LensProduct> searchLensProduct(@PathVariable final String keySearch) {
        return  lensProductRepo.findFirst50ByLensNoteContainsOrderByGmtCreateDesc(keySearch);
    }*/

    @RequestMapping(value ="searchLensProduct", method = RequestMethod.POST)
    public List<LensProduct> searchLensProduct(@RequestBody final QueryByClientShopAmountRequest req,final HttpServletRequest request) {

        if(req.getClientCode().equalsIgnoreCase(Utility.GODLIKE_CLIENTCODE)){
            return  lensProductRepo.findFirst50ByLensNoteContainsOrderByGmtCreateDesc(req.getGeneralPurpose());
        }else{
            return  lensProductRepo.findFirst50ByLensNoteContainsIgnoreCaseAndClientCodeOrderByGmtCreateDesc(req.getGeneralPurpose(), req.getClientCode());
        }
    }





    @RequestMapping(value = "getOrderByName", method = RequestMethod.POST)
    public List<Order> getOrderByName(@RequestBody final QueryByClientShopAmountRequest req,final HttpServletRequest request) {
        String keySearch = req.getGeneralPurpose();
        List<Order> orderList ;
        List<OrderDetail> orderDetailList ;

        if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE)
                && (req.getClientCode().equalsIgnoreCase("ALL") || req.getClientCode().equalsIgnoreCase(Utility.GODLIKE_ROLE)) ){

            orderList =  orderRepo.findFirst40ByShippingNameContainsIgnoreCaseOrderByGmtCreateDesc(keySearch);
            orderDetailList = orderDetailRepo.findFirst40ByNameContainsIgnoreCaseOrderByGmtCreateDesc(keySearch);

        }else if(req.getShopCode().equalsIgnoreCase("ALL")){
            orderList =  orderRepo.findFirst40ByClientCodeAndShippingNameContainsIgnoreCaseOrderByGmtCreateAsc(req.getClientCode(), keySearch);
            orderDetailList = orderDetailRepo.findFirst40ByClientCodeAndNameContainsIgnoreCaseOrderByGmtCreateDesc(req.getClientCode(), keySearch);

        }else{
            orderList =  orderRepo.findFirst40ByClientCodeAndShopCodeAndShippingNameContainsIgnoreCaseOrderByGmtCreateDesc(req.getClientCode(),req.getShopCode(), keySearch);
            orderDetailList = orderDetailRepo.findFirst40ByClientCodeAndShopCodeAndNameContainsIgnoreCaseOrderByGmtCreateDesc(req.getClientCode(), req.getShopCode(), keySearch);
        }

        return orderListBuilder(orderList, orderDetailList);
    }

    @RequestMapping(value = "getOrderByPhone", method = RequestMethod.POST)
    public List<Order> getOrderByPhone(@RequestBody final QueryByClientShopAmountRequest req, final HttpServletRequest request) {
        String keySearch = req.getGeneralPurpose();
        List<Order> orderList ;
        List<OrderDetail> orderDetailList ;

        if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE)
                && (req.getClientCode().equalsIgnoreCase("ALL") || req.getClientCode().equalsIgnoreCase(Utility.GODLIKE_ROLE)) ){

            orderList =  orderRepo.findFirst40ByShippingPhoneContainsOrderByGmtCreateDesc(keySearch);
            orderDetailList = orderDetailRepo.findFirst40ByPhoneContainsOrderByGmtCreateDesc(keySearch);

        }else if(req.getShopCode().equalsIgnoreCase("ALL")){
            orderList =  orderRepo.findFirst40ByClientCodeAndShippingPhoneContainsOrderByGmtCreateDesc(req.getClientCode(), keySearch);
            orderDetailList = orderDetailRepo.findFirst40ByClientCodeAndPhoneContainsOrderByGmtCreateDesc(req.getClientCode(), keySearch);

        }else{
            orderList =  orderRepo.findFirst40ByClientCodeAndShopCodeAndShippingPhoneContainsOrderByGmtCreateDesc(req.getClientCode(),req.getShopCode(), keySearch);
            orderDetailList = orderDetailRepo.findFirst40ByClientCodeAndShopCodeAndPhoneContainsOrderByGmtCreateDesc(req.getClientCode(), req.getShopCode(), keySearch);
        }

        return orderListBuilder(orderList, orderDetailList);
    }


    @RequestMapping(value = "orderByNamePhoneMngt", method = RequestMethod.POST)
    public List<Order> orderByNamePhoneMngt(@RequestBody final QueryByClientShopAmountRequest req,final HttpServletRequest request) {
        String regex = "[0-9 ]+";
        String keySearch = req.getGeneralPurpose();
        if(keySearch.matches(regex)){
            keySearch  = keySearch.replace(" ","");
            req.setGeneralPurpose(keySearch);
            return getOrderByPhone(req, request);
        }else{
            return getOrderByName(req, request);
        }
    }

    private List<Order> orderListBuilder(List<Order> orderList, List<OrderDetail> orderDetailList){
        List<Order> resultList = new ArrayList<>();
        for(Order or : orderList){
            if(resultList.contains(or)){
                continue;
            }
            resultList.add(or);
        }
        for (OrderDetail orderDetail : orderDetailList){
            Order or = orderBuilder(orderDetail.getOrderId(),orderDetail.getName(),orderDetail.getPhone(),orderDetail.getAddress(),orderDetail.getGmtCreate());
            if(resultList.contains(or)){
                continue;
            }
            or = orderRepo.findById(orderDetail.getOrderId()).get();
            resultList.add(or);
        }
        return resultList;
    }

    private Order orderBuilder(Integer orderId, String name, String phone, String address, Date gmtCreate){
        Order or = new Order(orderId,name,phone,address,gmtCreate,"STORE");
        or.getOrderDetails().add(new OrderDetail(name,phone,address,gmtCreate));
        return or;
    }







    //////////////////////////// lens product section///////////////////////////////
    @RequestMapping(value = "getlensProductForMgnt/{amount}", method = RequestMethod.GET)
    public List<LensProduct> getlensProductForMgnt(@PathVariable final int amount, final HttpServletRequest request) {
        List<LensProduct> lensProductList ;
        if(amount == Utility.FIRTST_TIME_LOAD_SIZE){
            lensProductList =  lensProductRepo.findFirst100ByOrderByGmtCreateDesc();
        }else{
            lensProductList = lensProductRepo.findAllByOrderByGmtCreateDesc();
        }
        return lensProductList;
    }

    @RequestMapping(value = "deleteLensProduct", method = RequestMethod.POST)
    public GenericResponse deleteLensProduct(@RequestBody final LensProduct one)  {
        lensProductRepo.delete(one);
        return new GenericResponse("deleteLensProduct",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "deleteManyLensProduct", method = RequestMethod.POST)
    public GenericResponse deleteManyLensProduct(@RequestBody final List<LensProduct> lensProducts)  {
        lensProductRepo.deleteAll(lensProducts);
        return new GenericResponse("deleteManyLensProduct",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "upsertLensProduct", method = RequestMethod.POST)
    public GeneralResponse<LensProduct> upsertLensProduct(@RequestBody final LensProduct one) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
        }
        one.setGmtModify(Utility.getCurrentDate());
        return new GeneralResponse(lensProductRepo.save(one),Utility.SUCCESS_ERRORCODE,"Save key success");
    }

    @RequestMapping(value = "prepareLensProductData", method = RequestMethod.GET)
    public GenericResponse prepareLensProductData() {
        List<OrderDetail> orderDetailList = orderDetailRepo.getDataForLensProduct();
        List<LensProduct> lensProductList = new ArrayList<>();
        String lensDeatil = "";
        String reading = "";
        String extInfo = "";
        for(OrderDetail detail : orderDetailList){
            reading = (detail.getReading() == null ? false : detail.getReading() ) ? ", đọc sách" : "";
            lensDeatil = "("+detail.getOdSphere() +" "+ detail.getOdCylinder()+")" +
                         "("+detail.getOsSphere() +" "+ detail.getOsCylinder()+")" +
                          reading;
            extInfo = detail.getOrderId() + "-" + detail.getId();
            lensProductList.add(new LensProduct(detail.getGmtCreate(),
                                                detail.getGmtModify(),
                                                detail.getLensNote(),
                                                lensDeatil,
                                                extInfo,
                                                detail.getLensPrice()
                                                ));
        }
        lensProductRepo.saveAll(lensProductList);

        return new GenericResponse("prepare lensProduct success",Utility.SUCCESS_ERRORCODE, "Success");
    }


    //////////////////////////// upload section///////////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile uploadfile, @RequestParam("oldName") String oldName,
                                             @RequestParam("type") String type, final HttpServletRequest request) {

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

    /**
     * ex: onlyAllowThisRole(request,Utility.SUPERADMIN_ROLE)
     * if true: allow continuing.
     * @param request
     * @param role
     * @return
     */
    private boolean onlyAllowThisRole(final HttpServletRequest request, String role){
        final Claims claims = (Claims) request.getAttribute("claims");
        if(((List<String>) claims.get("roles")).contains(role)){
            return true;
        }
        return false;
    }

    /**
     * purpose : check action has same clientCode with object
     * ex: client CTTT only can operate on CTTT object like order, member....
     * @param request
     * @param clientCode
     * @return
     */
    private boolean checkSameClientCode(final HttpServletRequest request, String clientCode){
        final Claims claims = (Claims) request.getAttribute("claims");
        Map<String,String> clientInfo = (Map<String, String>) claims.get("clientInfo");
        return clientCode.equalsIgnoreCase(clientInfo.get("clientCode"));
    }
}
