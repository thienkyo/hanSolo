package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findAllByOrderByGmtCreateDesc();

    List<OrderDetail> findFirst100ByOrderByGmtCreateDesc();

    List<OrderDetail> findByNameNotNullAndPhoneNotNullOrderByGmtCreateDesc();

    List<OrderDetail> findFirst100ByNameNotAndPhoneNotOrderByGmtCreateDesc(String name, String phone);

    List<OrderDetail> findByNameNotAndPhoneNotOrderByGmtCreateDesc(String name, String phone);


}