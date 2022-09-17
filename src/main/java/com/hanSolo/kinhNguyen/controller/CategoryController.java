package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Category;
import com.hanSolo.kinhNguyen.repository.CategoryRepository;
import com.hanSolo.kinhNguyen.repository.MemberRepository;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepo;

    @RequestMapping("active")
    public List<Category> getCategories() {

        return categoryRepo.findByStatusOrderByGmtModifyDesc(Utility.ACTIVE_STATUS);
    }
}
