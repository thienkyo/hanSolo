package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.SmsJob;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface SmsJobRepository extends PagingAndSortingRepository<SmsJob, Integer> {
    List<SmsJob> findAllByOrderByGmtCreateDesc();
    List<SmsJob> findByStatus(@NonNull Boolean status);
    Optional<SmsJob> findFirstByJobType(String jobType);

    Optional<SmsJob> findFirstByJobTypeAndStatus(String jobType, Boolean status);

    List<SmsJob> findByClientCodeOrderByGmtCreateDesc(String clientCode);

    List<SmsJob> findByClientCodeAndShopCodeOrderByGmtCreateDesc(@NonNull String clientCode, @NonNull String shopCode);


}