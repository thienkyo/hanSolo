package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    List<Category> findByStatusOrderByGmtModifyDesc(@NonNull Boolean status);

    List<Category> findByTypeAndStatusOrderByGmtModifyDesc(String type, Boolean status);

    Page<Category> findByStatusAndTypeOrderByGmtModifyDesc(Boolean status, String type, Pageable pageable);

}