package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
}