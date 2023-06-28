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
    List<OrderDetail> findFirst30ByNameContainsIgnoreCaseOrderByGmtCreateDesc(String name);


    //List<OrderDetail> findFirst30ByPhoneContains(String phone);
    List<OrderDetail> findFirst30ByPhoneContainsOrderByGmtCreateDesc(String phone);

    @Query("select distinct o from OrderDetail o where o.lensPrice <> 0 or o.otherNote is not null")
    List<OrderDetail> getDataForLensProduct();

}