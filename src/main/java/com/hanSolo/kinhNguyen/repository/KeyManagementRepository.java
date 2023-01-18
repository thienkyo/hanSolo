package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.KeyManagement;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface KeyManagementRepository extends PagingAndSortingRepository<KeyManagement, Integer> {
    List<KeyManagement> findAllByOrderByGmtCreateDesc();
}