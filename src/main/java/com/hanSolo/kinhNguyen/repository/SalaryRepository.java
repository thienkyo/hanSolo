package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Salary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

public interface SalaryRepository extends PagingAndSortingRepository<Salary, Integer> {

    List<Salary> findAllByOrderByGmtCreateDesc();
    List<Salary> findByContractIdOrderByYearDescMonthDesc(String contractId);

    List<Salary> findByYearAndMonth(String year, String month);
    List<Salary> findByYearAndMonthAndClientCodeAndShopCode(String year, String month, String clientCode, String shopCode);

    @Query("select c from Salary c where concat(c.year, c.month) in ?1")
    List<Salary> findByYearAndMonth(Collection<String> yearMonths);
}