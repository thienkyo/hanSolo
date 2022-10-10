package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {
    List<Order> findByShippingPhoneOrderByIdDesc(@NonNull String shippingPhone);
    List<Order> findFirst50ByOrderByGmtModifyDesc();
    List<Order> findAllByOrderByGmtModifyDesc();
}