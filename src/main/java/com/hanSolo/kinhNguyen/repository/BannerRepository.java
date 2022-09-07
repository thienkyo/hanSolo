package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Banner;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BannerRepository extends PagingAndSortingRepository<Banner, Integer> {
    List<Banner> findByStatusAndTypeOrderByIdDesc(Boolean status, String type);
    List<Banner> findByStatusOrderByIdDesc(Boolean status);

}