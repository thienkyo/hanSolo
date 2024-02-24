package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.facade.ClientInterface;
import com.hanSolo.kinhNguyen.models.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ClientRepository extends PagingAndSortingRepository<Client, Integer> {
    List<Client> findAllByOrderByGmtCreateDesc();

    Client findFirstByClientCode(String clientCode);

    ClientInterface queryFirstByClientCode(String clientCode);

    List<Client> findByClientCode(String clientCode);

    List<Client> findByStatus(String status);

    @Transactional
    @Modifying
    @Query("update Client c set c.status = ?1, c.gmtModify = ?2 where c.id = ?3")
    int updateStatusAndGmtModifyById(String status, Date gmtModify, Integer id);

}