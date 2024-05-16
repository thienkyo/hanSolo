package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findAllByOrderByGmtCreateDesc();
    List<OrderDetail> findFirst100ByNameNotAndPhoneNotOrderByGmtCreateDesc(String name, String phone);
    List<OrderDetail> findByNameNotAndPhoneNotOrderByGmtCreateDesc(String name, String phone);

    //List<OrderDetail> findFirst30ByNameContainsIgnoreCase(String name);
    List<OrderDetail> findFirst40ByNameContainsIgnoreCaseOrderByGmtCreateDesc(String name);


    //List<OrderDetail> findFirst30ByPhoneContains(String phone);
    List<OrderDetail> findFirst40ByPhoneContainsOrderByGmtCreateDesc(String phone);

    @Query("select distinct o from OrderDetail o where o.lensPrice <> 0 or o.otherNote is not null")
    List<OrderDetail> getDataForLensProduct();

    //// client and shop
    List<OrderDetail> findFirst40ByClientCodeAndNameContainsIgnoreCaseOrderByGmtCreateDesc(String clientCode, String name);

    List<OrderDetail> findFirst40ByClientCodeAndPhoneContainsOrderByGmtCreateDesc(String clientCode, String phone);

    List<OrderDetail> findFirst40ByClientCodeAndShopCodeAndNameContainsIgnoreCaseOrderByGmtCreateDesc(String clientCode, String shopCode, String name);

    List<OrderDetail> findFirst40ByClientCodeAndShopCodeAndPhoneContainsOrderByGmtCreateDesc(String clientCode, String shopCode, String phone);

    List<OrderDetail> findFirst40ByClientCodeAndPhoneOrderByGmtCreateDesc(String clientCode, String phone);

    List<OrderDetail> findFirst15ByClientCodeOrderByGmtCreateDesc(String clientCode);


}