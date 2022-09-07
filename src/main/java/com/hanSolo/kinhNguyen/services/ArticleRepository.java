package com.hanSolo.kinhNguyen.services;

import com.hanSolo.kinhNguyen.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Integer> {
    Article findFirstByNameLikeIgnoreCaseOrderByGmtCreateAsc(String name);

}