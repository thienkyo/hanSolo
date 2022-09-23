package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Supplier;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SupplierRepository extends PagingAndSortingRepository<Supplier, Integer> {
    List<Supplier> findByOrderByGmtModifyDesc();
}