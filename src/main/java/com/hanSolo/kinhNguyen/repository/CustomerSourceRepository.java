package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.CustomerSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerSourceRepository extends JpaRepository<CustomerSource, Integer> {
    List<CustomerSource> findAllByOrderByGmtCreateDesc();
}