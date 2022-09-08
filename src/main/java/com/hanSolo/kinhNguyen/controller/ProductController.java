package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Product;
import com.hanSolo.kinhNguyen.repository.ProductRepository;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository prodRepo;

    @RequestMapping("homeProduct")
    public List<Product> getHomeProduct() {
        List<Product> newProducts = prodRepo.findFirst8ByStatusAndDiscountOrderByGmtModifyDesc(Utility.ACTIVE_STATUS,0);
        List<Product> discountProducts = prodRepo.findFirst4ByStatusAndDiscountGreaterThanOrderByGmtModifyDesc(Utility.ACTIVE_STATUS,1);
        newProducts.addAll(discountProducts);
        Collections.shuffle(newProducts);
        //List<Product> result = StreamSupport.stream(newProducts.spliterator(), false).collect(Collectors.toList());

        return newProducts;
    }
/*
    @RequestMapping("{status}")
    public List<Product> getProductByStatus(@PathVariable final int status) {//@RequestParam(value = "status")
        return prodRepo.findByStatusOrderByModDateDesc(status);
    }

    @RequestMapping("first6")
    public List<Product> getFisrt6Product() {//@RequestParam(value = "status")
        return prodRepo.findFirst6ByStatusOrderByModDateDesc(UtilityConstant.ACTIVE_STATUS);
    }*/
}
