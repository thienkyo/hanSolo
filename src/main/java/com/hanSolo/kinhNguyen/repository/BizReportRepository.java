package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.BizReport;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BizReportRepository extends PagingAndSortingRepository<BizReport, Integer> {
    List<BizReport> findByOrderByYearDescMonthDesc();

}