package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Article;
import com.hanSolo.kinhNguyen.repository.ArticleRepository;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepo;

    @RequestMapping("homeArticle")
    public List<Article> getActiveArticle() {
        return articleRepo.findFirst3ByStatusOrderByGmtModifyDesc(Utility.ACTIVE_STATUS);
    }

    @RequestMapping(value = "getArticlePage/{pageNumber}", method = RequestMethod.GET)
    public Page<Article> getArticlePage(@PathVariable Integer pageNumber) {
        //Pageable request = new PageRequest(pageNumber - 1, UtilityConstant.BLOG_PAGE_SIZE, Sort.Direction.DESC, "articleId");
        Pageable request = PageRequest.of(pageNumber - 1, Utility.BLOG_PAGE_SIZE, Sort.Direction.DESC, "id");
        return articleRepo.findByStatus(Utility.ACTIVE_STATUS, request);
    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Article getOneActiveArticle(@PathVariable final int id) {
        return articleRepo.findByStatusAndId( Utility.ACTIVE_STATUS,id);
    }
}
