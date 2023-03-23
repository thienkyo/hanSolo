package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.CustomerSourceReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CustomerSourceReportRepository extends PagingAndSortingRepository<CustomerSourceReport, Integer> {
    List<CustomerSourceReport> findAllByOrderByGmtCreateDesc();
    List<CustomerSourceReport> findByYearAndMonth(String year, String month);
    List<CustomerSourceReport> findByOrderByYearDescMonthDescCustomerSourceIdAsc();
 }