package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.SmsQueue;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SmsQueueRepository extends PagingAndSortingRepository<SmsQueue, Integer> {
    List<SmsQueue> findByOrderByGmtCreateDesc();

}