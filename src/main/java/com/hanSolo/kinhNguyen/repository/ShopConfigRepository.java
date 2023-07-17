package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.ShopConfig;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ShopConfigRepository extends PagingAndSortingRepository<ShopConfig, Integer> {
    ShopConfig findFirstByOrderByIdAsc();
}