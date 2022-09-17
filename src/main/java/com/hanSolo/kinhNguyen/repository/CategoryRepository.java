package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    List<Category> findByStatusOrderByGmtModifyDesc(@NonNull Boolean status);
}