package com.hanSolo.kinhNguyen.cacheCenter;

import com.hanSolo.kinhNguyen.facade.ShopInterface;
import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.models.Shop;
import com.hanSolo.kinhNguyen.utility.Utility;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonCache {

    ///// config /////
    /**
     * the last time sending sms
     */
    public static Date LAST_SMS_HEARTBEAT_TIME;

    /**
     * the last time build data.
     */
    public static Date LAST_PREPARE_DATA_HEARTBEAT_TIME;

    /**
     * control api prepareSmsData: to prepare data sms sending true = allow
     */
    public static boolean SMS_DATA_PREPARE_CONTROL = true;

    /**
     * control api getQueueSms: get data for sms sending true = allow
     */
    public static boolean SMS_SEND_CONTROL = true;

    /**
     * member list : help to check user status.
     */
    public static Map<String, Member> LOGIN_MEMBER_LIST = new HashMap<>(Utility.LOGIN_MEMBER_LIST_SIZE);

    public static Map<String, List<Shop>> CLIENT_SHOP_LIST = new HashMap<>(Utility.LOGIN_MEMBER_LIST_SIZE);

    static {
        try {
            LAST_SMS_HEARTBEAT_TIME = Utility.getCurrentDate();
            LAST_PREPARE_DATA_HEARTBEAT_TIME = Utility.getCurrentDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean checkValidShop(String clientCode, String shopCode){
        List<Shop> shopList = CLIENT_SHOP_LIST.getOrDefault(clientCode, null);

        if(shopList == null){
            return false;
        }

        List<Shop> filteredList = shopList.stream()
                .filter(x -> x.getShopCode().contains(shopCode))
                .collect(Collectors.toList());
        return !filteredList.isEmpty();
    }

}
