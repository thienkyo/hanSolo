package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.UsedCoupons;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UsedCouponsRepository extends PagingAndSortingRepository<UsedCoupons, Integer> {
    List<UsedCoupons> findByOrderByOrderDateDesc();

}