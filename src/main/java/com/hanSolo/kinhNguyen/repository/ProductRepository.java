package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    List<Product> findFirst8ByStatusOrderByGmtModifyDesc(int status);
    // for homePage, get 8 new product
    List<Product> findFirst8ByStatusAndDiscountOrderByGmtModifyDesc(Boolean status, Integer discount);
    // for homePage, get 4 discount product
    List<Product> findFirst4ByStatusAndDiscountGreaterThanOrderByGmtModifyDesc(Boolean status, Integer discount);
}