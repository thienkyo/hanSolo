package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Integer> {
    List<Article> findFirst3ByStatusOrderByGmtModifyDesc(boolean status);

    List<Article> findByNameContainsIgnoreCaseAndStatus(String name, Boolean status);

    List<Article> findByStatusOrderByGmtModifyDesc(boolean status);

    Article findByStatusAndId(@NonNull boolean status, @NonNull Integer id);

    Page<Article> findByStatus(boolean status, Pageable pageable);

}