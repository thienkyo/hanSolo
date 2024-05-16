package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.BizExpense;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface BizExpenseRepository extends PagingAndSortingRepository<BizExpense, Integer> {

    List<BizExpense> findFirst100ByOrderByGmtCreateDesc();
    List<BizExpense> findAllByOrderByGmtCreateDesc();

    @Transactional
    @Modifying
    @Query("update BizExpense b set b.status = ?1, b.gmtModify = ?2 where b.id = ?3")
    int updateStatusAndGmtModifyById(Integer status, Date gmtModify, Integer id);

    @Query("select b from BizExpense b where b.gmtCreate between ?1 and ?2")
    List<BizExpense> findByGmtCreateBetween(Date gmtCreateStart, Date gmtCreateEnd);

    List<BizExpense> findByClientCodeAndGmtCreateBetween(String clientCode, Date gmtCreateStart, Date gmtCreateEnd);

    List<BizExpense> findByClientCodeAndShopCodeAndGmtCreateBetween(String clientCode, String shopCode, Date gmtCreateStart, Date gmtCreateEnd);

    List<BizExpense> findFirst100ByClientCodeOrderByGmtCreateDesc(String clientCode);
    List<BizExpense> findFirst100ByClientCodeAndShopCodeOrderByGmtCreateDesc(String clientCode, String shopCode);

    List<BizExpense> findByClientCodeOrderByGmtCreateDesc(String clientCode);
    List<BizExpense> findByClientCodeAndShopCodeOrderByGmtCreateDesc(String clientCode, String shopCode);

    List<BizExpense> findFirst15ByClientCodeOrderByGmtCreateDesc(String clientCode);

}