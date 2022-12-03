package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {
    List<Order> findByShippingPhoneOrderByIdDesc(@NonNull String shippingPhone);

    List<Order> findFirst50ByOrderByGmtCreateDesc();

    List<Order> findFirst100ByOrderByGmtCreateDesc();

    List<Order> findAllByOrderByGmtCreateDesc();

    @Transactional
    @Modifying
    @Query("update Order o set o.status = ?1, o.gmtModify = ?2 where o.id = ?3")
    int updateStatusAndGmtModifyById(Integer status, Date gmtModify, Integer id);

    @Transactional
    @Modifying
    @Query("update Order o set o.gmtModify = ?1, o.cusSource = ?2 where o.id = ?3")
    int updateGmtModifyAndCusSourceById(@NonNull Date gmtModify, @NonNull Integer cusSource, @NonNull Integer id);

    List<Order> findByGmtCreateBetween(@NonNull Date gmtCreateStart, @NonNull Date gmtCreateEnd);



}