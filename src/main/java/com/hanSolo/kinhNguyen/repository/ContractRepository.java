package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Contract;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ContractRepository extends PagingAndSortingRepository<Contract, Integer> {

    List<Contract> findAllByOrderByGmtCreateDesc();
}