package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.CustomerSourceReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface CustomerSourceReportRepository extends PagingAndSortingRepository<CustomerSourceReport, Integer> {
    List<CustomerSourceReport> findAllByOrderByGmtCreateDesc();
    List<CustomerSourceReport> findByYearAndMonth(String year, String month);
    List<CustomerSourceReport> findByOrderByYearDescMonthDescCustomerSourceIdAsc();

    List<CustomerSourceReport> findByClientCodeAndShopCodeAndYearOrderByYearDescMonthDescCustomerSourceIdAsc(
            String clientCode, String shopCode, String year);

    List<CustomerSourceReport> findByClientCodeAndYearOrderByYearDescMonthDescCustomerSourceIdAsc(
            String clientCode, String Year);

    @Query("select c from CustomerSourceReport c where concat(c.year, c.month) in ?1")
    List<CustomerSourceReport> findByYearAndMonth(Collection<String> yearMonths);






 }