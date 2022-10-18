package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.*;
import com.hanSolo.kinhNguyen.repository.*;
import com.hanSolo.kinhNguyen.response.CategoryResponse;
import com.hanSolo.kinhNguyen.response.CouponResponse;
import com.hanSolo.kinhNguyen.response.GenericResponse;
import com.hanSolo.kinhNguyen.response.SupplierResponse;
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

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    SupplierRepository supplierRepo;

    @Autowired
    private ProductRepository prodRepo;

    @Autowired
    private BannerRepository bannerRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private CouponRepository couponRepo;

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired private OrderRepository orderRepo;

    @Autowired
    private Environment env;

    ////////////////////////////category section//////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getAllCategories", method = RequestMethod.GET)
    public List<Category> getAllCategories(final HttpServletRequest request) throws ServletException {
        return categoryRepo.findByOrderByIdDesc();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "upsertCategory", method = RequestMethod.POST)
    public CategoryResponse upsertCategory(@RequestBody final Category cate, final HttpServletRequest request) throws ServletException {
        Category newCate = categoryRepo.save(cate);
        return new CategoryResponse(newCate,Utility.SUCCESS_ERRORCODE,"Success");
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "deleteCategory", method = RequestMethod.POST)
    public GenericResponse deleteCategory(@RequestBody final Category cate, final HttpServletRequest request) throws ServletException {
        categoryRepo.delete(cate);
        return new GenericResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    ////////////////////////////supplier section//////////////////////////

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getAllSuppliers", method = RequestMethod.GET)
    public List<Supplier> getAllSuppliers(final HttpServletRequest request) throws ServletException {
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
                if(amount==50){
                    productList =  prodRepo.findFirst50ByOrderByGmtModifyDesc();
                }else{
                    productList =  prodRepo.findByOrderByGmtModifyDesc();
                }
            }else{
                if(amount==50){
                    productList =  prodRepo.findFirst50ByCategories_IdOrderByGmtModifyDesc(cateId);
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

    //////////////////////////// banner ///////////////////////////////

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

    //////////////////////////////Member section/////////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getMemberForMgnt/{amount}", method = RequestMethod.GET)
    public List<Member> getMemberForMgnt(@PathVariable final int amount, final HttpServletRequest request) throws ServletException {
        List<Member> memberList;
            if(amount==50){
                memberList =  memberRepo.findFirst50ByOrderByGmtModifyDesc();
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

    //////////////////////////// Coupon ///////////////////////////////
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getAllCoupons", method = RequestMethod.GET)
    public List<Coupon> getAllCoupons(final HttpServletRequest request) throws ServletException {
        return couponRepo.findByOrderByGmtModifyDesc();
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

    //////////////////////////// blog/article ///////////////////////////////
    @RequestMapping(value = "getArticlesForMgnt/{amount}", method = RequestMethod.GET)
    public List<Article> getArticlesForMgnt(@PathVariable final int amount) throws ServletException {
        List<Article> articleList;
        if(amount==50){
            articleList =  articleRepo.findFirst50ByOrderByGmtModifyDesc();
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
    public List<Order> getOrdersForMgnt(@PathVariable final int amount, final HttpServletRequest request) throws ServletException {
        List<Order> orderList ;
        if(amount==50){
            orderList =  orderRepo.findFirst50ByOrderByGmtModifyDesc();
        }else{
            orderList = orderRepo.findAllByOrderByGmtModifyDesc();
        }
        return orderList;
    }

    @RequestMapping(value = "updateOrder", method = RequestMethod.POST)
    public GenericResponse updateOrder(@RequestBody final Order order) throws ServletException, ParseException {
        orderRepo.updateStatusAndGmtModifyById(order.getStatus(),Utility.getCurrentDate(),order.getId());
        return new GenericResponse("upsert_order_success",Utility.SUCCESS_ERRORCODE,"Success");
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
