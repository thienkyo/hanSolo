package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Contract;
import com.hanSolo.kinhNguyen.models.Member;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ContractRepository extends PagingAndSortingRepository<Contract, Integer> {

    List<Contract> findAllByOrderByGmtCreateDesc();
    List<Contract> findFirst100ByOrderByGmtCreateDesc();
    List<Contract> findFirst100ByClientCodeOrderByGmtCreateDesc(String clientCode);
    List<Contract> findFirst100ByClientCodeAndShopCodeOrderByGmtCreateDesc(String clientCode, String shopCode);
    List<Contract> findByClientCodeOrderByGmtCreateDesc(String clientCode);
    List<Contract> findByClientCodeAndShopCodeOrderByGmtCreateDesc(String clientCode, String shopCode);
}