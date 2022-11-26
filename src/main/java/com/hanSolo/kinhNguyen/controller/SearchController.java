package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.facade.ProductInterface;
import com.hanSolo.kinhNguyen.models.Article;
import com.hanSolo.kinhNguyen.models.Product;
import com.hanSolo.kinhNguyen.repository.ArticleRepository;
import com.hanSolo.kinhNguyen.repository.ProductRepository;
import com.hanSolo.kinhNguyen.response.SearchResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ProductRepository prodRepo;

    @Autowired
    private ArticleRepository articleRepo;

    @RequestMapping("{keySearch}")
    public List<SearchResponse> search(@PathVariable final String keySearch) {
        List<ProductInterface> prodList = prodRepo.findByNameContainsIgnoreCaseAndStatus(keySearch, Utility.ACTIVE_STATUS);
        List<SearchResponse> resultList = new ArrayList<>();
        prodList.forEach(item->{
            SearchResponse temp = new SearchResponse("Frame",item.getName(), item.getId()+"", item.getThumbnail(), Utility.SUCCESS_ERRORCODE, "Query success");
            resultList.add(temp);
        });

        List<Article> articleList = articleRepo.findByNameContainsIgnoreCaseAndStatus(keySearch, Utility.ACTIVE_STATUS);
        articleList.forEach(item->{
            SearchResponse temp = new SearchResponse("Blog",item.getName(), item.getId()+"", item.getThumbnail(), Utility.SUCCESS_ERRORCODE, "Query success");
            resultList.add(temp);
        });

        return resultList;
    }

    @RequestMapping("product/{keySearch}")
    public List<ProductInterface> searchProduct(@PathVariable final String keySearch) {
        return prodRepo.findByNameContainsIgnoreCaseAndStatus(keySearch, Utility.ACTIVE_STATUS);
    }

    @RequestMapping("productMngt/{keySearch}")
    public List<ProductInterface> productMngt(@PathVariable final String keySearch) {
        return prodRepo.findByNameContainsIgnoreCase(keySearch);
    }
}
