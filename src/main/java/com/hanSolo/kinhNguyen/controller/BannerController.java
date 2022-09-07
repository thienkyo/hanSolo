package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Banner;
import com.hanSolo.kinhNguyen.repository.BannerRepository;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerRepository bannerRepo;

    @RequestMapping("")
    public List<Banner> getHomeBanner() {
        return bannerRepo.findByStatusOrderByIdDesc(Utility.ACTIVE_STATUS);
    }
}
