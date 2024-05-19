package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.ProgramResult;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProgramResultRepository extends PagingAndSortingRepository<ProgramResult, Integer> {
    List<ProgramResult> findByClientCodeAndStatus(String clientCode, Boolean status);
    List<ProgramResult> findByClientCodeOrderByGmtCreateDesc(String clientCode);
    List<ProgramResult> findByClientCodeAndShopCodeOrderByGmtCreateDesc(String clientCode, String shopCode);
}