package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.SmsQueue;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SmsQueueRepository extends PagingAndSortingRepository<SmsQueue, Integer> {
    List<SmsQueue> findAllByOrderByGmtCreateDesc();
    Optional<SmsQueue> findFirstByStatusOrderByGmtCreateAsc(@NonNull String status);

    Optional<SmsQueue> findFirstByStatusOrderByWeightDescGmtCreateAsc(@NonNull String status);

    List<SmsQueue> findFirst100ByOrderByGmtCreateDesc();
    List<SmsQueue> findFirst100ByStatusOrderByGmtCreateAsc(@NonNull String status);

    Optional<SmsQueue> findFirstByJobTypeAndStatusOrderByGmtModifyDesc(String jobType, String status);

    Optional<SmsQueue> findFirstByJobTypeAndStatusInOrderByGmtModifyDesc(String jobType, Collection<String> statuses);

    List<SmsQueue> findByClientCodeOrderByGmtCreateDesc(@NonNull String clientCode);
    List<SmsQueue> findFirst100ByClientCodeOrderByGmtCreateDesc(@NonNull String clientCode);

    List<SmsQueue> findByClientCodeAndShopCodeOrderByGmtCreateDesc(@NonNull String clientCode, @NonNull String shopCode);
    List<SmsQueue> findFirst100ByClientCodeAndShopCodeOrderByGmtCreateDesc(@NonNull String clientCode, @NonNull String shopCode);

}