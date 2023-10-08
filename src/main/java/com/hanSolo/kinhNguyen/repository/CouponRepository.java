package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Coupon;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends PagingAndSortingRepository<Coupon, Integer> {
    Optional<Coupon> findByCode(@NonNull String code);
    List<Coupon> findByOrderByGmtModifyDesc();
    List<Coupon> findAllByOrderByGmtCreateDesc();
    Optional<Coupon> findByCodeAndCouponType(@NonNull String code, @NonNull String couponType);

    List<Coupon> findByClientCodeOrderByGmtCreateDesc(String clientCode);

    Optional<Coupon> findByCodeAndCouponTypeAndClientCode(String code, String couponType, String clientCode);


}