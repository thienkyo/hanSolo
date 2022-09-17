package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {
}