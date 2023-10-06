package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.LensProduct;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface LensProductRepository extends PagingAndSortingRepository<LensProduct, Integer> {
    List<LensProduct> findFirst50ByLensNoteContainsOrderByGmtCreateDesc(String lensNote);
    List<LensProduct> findFirst100ByOrderByGmtCreateDesc();
    List<LensProduct> findAllByOrderByGmtCreateDesc();
    List<LensProduct> findByLensNoteAndSellPrice(String lensNote, Integer sellPrice);
    List<LensProduct> findFirstByLensDetailAndSellPrice(String lensDetail, Integer sellPrice);

    Optional<LensProduct> findFirstByExtInfo(String extInfo);

    /// clientCode shopCode
    List<LensProduct> findFirst50ByLensNoteContainsIgnoreCaseAndClientCodeOrderByGmtCreateDesc(String lensNote, String clientCode);



}