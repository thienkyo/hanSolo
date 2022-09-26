package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Category;
import com.hanSolo.kinhNguyen.models.Product;
import com.hanSolo.kinhNguyen.models.Supplier;
import com.hanSolo.kinhNguyen.repository.CategoryRepository;
import com.hanSolo.kinhNguyen.repository.ProductRepository;
import com.hanSolo.kinhNguyen.repository.SupplierRepository;
import com.hanSolo.kinhNguyen.response.CategoryResponse;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/mgnt")
public class ManagementController {

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    SupplierRepository supplierRepo;

    @Autowired
    private ProductRepository prodRepo;

    @Autowired private Environment env;

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
    public SupplierResponse upsertSupplier(@RequestBody final Supplier supplier, final HttpServletRequest request) throws ServletException {
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
