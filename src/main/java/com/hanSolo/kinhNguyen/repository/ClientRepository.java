package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.facade.ClientInterface;
import com.hanSolo.kinhNguyen.models.Client;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ClientRepository extends PagingAndSortingRepository<Client, Integer> {
    List<Client> findAllByOrderByGmtCreateDesc();

    Client findFirstByClientCode(String clientCode);

    ClientInterface queryFirstByClientCode(String clientCode);

    List<Client> findByClientCode(String clientCode);

}