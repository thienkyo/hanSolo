package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.BizReport;
import com.hanSolo.kinhNguyen.models.Contract;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BizReportRepository extends PagingAndSortingRepository<BizReport, Integer> {
    List<BizReport> findByOrderByGmtCreateDesc();
    List<BizReport> findByOrderByYearDescMonthDesc();

    List<BizReport> findFirst100ByOrderByGmtCreateDesc();
    List<BizReport> findFirst100ByClientCodeOrderByGmtCreateDesc(String clientCode);
    List<BizReport> findFirst100ByClientCodeAndShopCodeOrderByGmtCreateDesc(String clientCode, String shopCode);
    List<BizReport> findAllByOrderByGmtCreateDesc();
    List<BizReport> findByClientCodeOrderByGmtCreateDesc(String clientCode);
    List<BizReport> findByClientCodeAndShopCodeOrderByGmtCreateDesc(String clientCode, String shopCode);

    List<BizReport> findByClientCodeOrderByYearDescMonthDesc(String clientCode);
    List<BizReport> findByClientCodeAndShopCodeOrderByYearDescMonthDesc(String clientCode, String shopCode);

}