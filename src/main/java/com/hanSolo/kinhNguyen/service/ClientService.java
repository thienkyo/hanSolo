package com.hanSolo.kinhNguyen.service;

import com.hanSolo.kinhNguyen.cacheCenter.CommonCache;
import com.hanSolo.kinhNguyen.models.Client;
import com.hanSolo.kinhNguyen.repository.ClientRepository;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
    @Autowired
    private ClientRepository clientRepo;


    public void updateClientStatus(Client client) throws ParseException {
        if(StringUtils.equalsIgnoreCase(client.getStatus(), Utility.CLIENT_STATUS_INACTIVE) ){
            CommonCache.CLIENT_LIST.remove(client.getClientCode());
        }else if(StringUtils.equalsIgnoreCase(client.getStatus(), Utility.CLIENT_STATUS_ACTIVE) ){
            CommonCache.CLIENT_LIST.put(client.getClientCode(), client);
        }
        LOGGER.info("updateClientStatus CLIENT_LIST :" + CommonCache.CLIENT_LIST.size());
        clientRepo.updateStatusAndGmtModifyById(client.getStatus(), Utility.getCurrentDate(),client.getId());
    }
}
