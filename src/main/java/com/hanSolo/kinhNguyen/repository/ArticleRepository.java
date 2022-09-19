package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Article;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Integer> {
    List<Article> findFirst3ByStatusOrderByGmtModifyDesc(boolean status);

    List<Article> findByNameContainsIgnoreCaseAndStatus(String name, Boolean status);

}