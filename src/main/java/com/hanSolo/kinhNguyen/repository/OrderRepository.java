package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {
    List<Order> findByShippingPhoneOrderByIdDesc(@NonNull String shippingPhone);
    List<Order> findFirst100ByOrderByGmtCreateDesc();
    List<Order> findAllByOrderByGmtCreateDesc();
    List<Order> findFirst40ByShippingNameContainsIgnoreCaseOrderByGmtCreateDesc(String shippingName);
    List<Order> findFirst40ByShippingPhoneContainsOrderByGmtCreateDesc(String shippingPhone);

    @Transactional
    @Modifying
    @Query("update Order o set o.status = ?1, o.gmtModify = ?2 where o.id = ?3")
    int updateStatusAndGmtModifyById(Integer status, Date gmtModify, Integer id);

    @Transactional
    @Modifying
    @Query("update Order o set o.gmtModify = ?1, o.cusSource = ?2 where o.id = ?3")
    int updateGmtModifyAndCusSourceById(@NonNull Date gmtModify, @NonNull Integer cusSource, @NonNull Integer id);

    long countByGmtCreateBetweenAndCusSource(@NonNull Date gmtCreateStart, @NonNull Date gmtCreateEnd, @Nullable Integer cusSource);

    List<Order> findByGmtCreateBetweenAndCusSourceNotNull(@NonNull Date gmtCreateStart, @NonNull Date gmtCreateEnd);



    List<Order> findByClientCodeOrderByGmtCreateDesc(String clientCode);
    List<Order> findByClientCodeAndShopCodeOrderByGmtCreateDesc(String clientCode, String shopCode);
    List<Order> findFirst100ByClientCodeOrderByGmtCreateDesc(String clientCode);
    List<Order> findFirst100ByClientCodeAndShopCodeOrderByGmtCreateDesc(String clientCode, String shopCode);
    Optional<Order> findByIdAndClientCode(Integer id, String clientCode);

    List<Order> findFirst40ByClientCodeAndShippingNameContainsIgnoreCaseOrderByGmtCreateAsc(String clientCode, String shippingName);
    List<Order> findFirst40ByClientCodeAndShippingPhoneContainsOrderByGmtCreateDesc(String clientCode, String shippingPhone);

    List<Order> findFirst40ByClientCodeAndShopCodeAndShippingNameContainsIgnoreCaseOrderByGmtCreateDesc(String clientCode, String shopCode, String shippingName);

    List<Order> findFirst40ByClientCodeAndShopCodeAndShippingPhoneContainsOrderByGmtCreateDesc(String clientCode, String shopCode, String shippingPhone);

    List<Order> findFirst40ByClientCodeAndShippingPhoneOrderByGmtCreateDesc(String clientCode, String shippingPhone);

    List<Order> findByClientCodeAndShopCodeAndGmtCreateBetween(@NonNull String clientCode, @NonNull String shopCode, Date gmtCreateStart, Date gmtCreateEnd);

    List<Order> findByIdInAndClientCode(Collection<Integer> ids, @NonNull String clientCode);

    List<Order> findByGmtCreateBetween(@NonNull Date gmtCreateStart, @NonNull Date gmtCreateEnd);


}