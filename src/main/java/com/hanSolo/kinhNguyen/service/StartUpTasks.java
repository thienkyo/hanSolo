package com.hanSolo.kinhNguyen.service;

import com.hanSolo.kinhNguyen.cacheCenter.CommonCache;
import com.hanSolo.kinhNguyen.models.Client;
import com.hanSolo.kinhNguyen.repository.ClientRepository;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartUpTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartUpTasks.class);
    @Autowired
    private ClientRepository clientRepo;

    @EventListener(ApplicationReadyEvent.class)
    public  void startUp(){
        // 1. load active client list
        loadActiveClientList();
    }

    private void loadActiveClientList(){
        List<Client> clients = clientRepo.findByStatus(Utility.CLIENT_STATUS_ACTIVE);
        clients.forEach(one -> CommonCache.CLIENT_LIST.put(one.getClientCode(),one));
        LOGGER.info("startUp CLIENT_LIST :" + CommonCache.CLIENT_LIST.size());
    }
}
