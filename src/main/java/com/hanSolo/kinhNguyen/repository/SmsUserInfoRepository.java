package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.SmsUserInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface SmsUserInfoRepository extends PagingAndSortingRepository<SmsUserInfo, Integer> {
    Optional<SmsUserInfo> findByPhone(String phone);
}