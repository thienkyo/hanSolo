package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Article;
import com.hanSolo.kinhNguyen.repository.ArticleRepository;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
