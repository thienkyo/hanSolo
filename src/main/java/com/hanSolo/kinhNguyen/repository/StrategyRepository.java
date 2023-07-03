package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Strategy;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StrategyRepository extends PagingAndSortingRepository<Strategy, Integer> {
    List<Strategy> findFirst100ByOrderByGmtCreateDesc();

    List<Strategy> findAllByOrderByGmtCreateDesc();
}