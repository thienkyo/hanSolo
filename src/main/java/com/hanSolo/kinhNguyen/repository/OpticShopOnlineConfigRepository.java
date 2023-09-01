package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.OpticShopOnlineConfig;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface OpticShopOnlineConfigRepository extends PagingAndSortingRepository<OpticShopOnlineConfig, UUID> {
    OpticShopOnlineConfig findFirstByOrderByNameDesc();

}