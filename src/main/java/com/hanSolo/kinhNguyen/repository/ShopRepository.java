package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Shop;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ShopRepository extends PagingAndSortingRepository<Shop, Integer> {
    List<Shop> findByClientIdOrderByGmtCreateDesc(Integer clientId);

}