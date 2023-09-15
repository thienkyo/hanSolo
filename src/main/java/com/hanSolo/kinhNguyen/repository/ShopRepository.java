package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.DTO.ShopInfo;
import com.hanSolo.kinhNguyen.facade.ShopInterface;
import com.hanSolo.kinhNguyen.models.Shop;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ShopRepository extends PagingAndSortingRepository<Shop, Integer> {
    List<Shop> findByClientIdOrderByGmtCreateDesc(Integer clientId);
    List<Shop> findByClientCodeOrderByGmtCreateDesc(String clientCode);

    List<ShopInterface> queryByClientCodeOrderByGmtCreateDesc(String clientCode);

    List<Shop> findByClientCodeAndShopCode(String clientCode, String shopCode);

    List<ShopInterface> queryByOrderByGmtCreateAsc();

    List<Shop> findByOrderByGmtCreateDesc();

}