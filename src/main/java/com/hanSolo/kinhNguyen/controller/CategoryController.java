package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Category;
import com.hanSolo.kinhNguyen.models.Product;
import com.hanSolo.kinhNguyen.repository.CategoryRepository;
import com.hanSolo.kinhNguyen.repository.MemberRepository;
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
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepo;

    @RequestMapping("active")
    public List<Category> getCategories() {

        return categoryRepo.findByStatusOrderByGmtModifyDesc(Utility.ACTIVE_STATUS);
    }

    @RequestMapping("collections")
    public List<Category> getCollections() {

        return categoryRepo.findByTypeAndStatusOrderByGmtModifyDesc("COLLECTION",Utility.ACTIVE_STATUS);
    }

    @RequestMapping(value = "getCollectionPage/{pageNumber}", method = RequestMethod.GET)
    public Page<Category> getProductPage(@PathVariable Integer pageNumber) {
        Pageable request = PageRequest.of(pageNumber - 1, Utility.COLLECTION_PAGE_SIZE, Sort.Direction.DESC, "gmtModify");
        return categoryRepo.findByStatusAndTypeOrderByGmtModifyDesc(Utility.ACTIVE_STATUS,"COLLECTION", request);
    }
}
