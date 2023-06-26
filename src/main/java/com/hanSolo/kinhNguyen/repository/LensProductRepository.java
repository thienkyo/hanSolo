package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.LensProduct;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface LensProductRepository extends PagingAndSortingRepository<LensProduct, Integer> {
    List<LensProduct> findFirst50ByLensNoteContainsOrderByGmtCreateDesc(String lensNote);
    List<LensProduct> findFirst100ByOrderByGmtCreateDesc();
    List<LensProduct> findAllByOrderByGmtCreateDesc();
    List<LensProduct> findByLensNoteAndSellPrice(String lensNote, Integer sellPrice);
}