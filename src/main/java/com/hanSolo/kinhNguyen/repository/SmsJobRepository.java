package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.SmsJob;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface SmsJobRepository extends PagingAndSortingRepository<SmsJob, Integer> {
    List<SmsJob> findAllByOrderByGmtCreateDesc();

    List<SmsJob> findByStatus(@NonNull Boolean status);

}