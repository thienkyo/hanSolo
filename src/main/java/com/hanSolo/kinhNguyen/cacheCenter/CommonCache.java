package com.hanSolo.kinhNguyen.cacheCenter;

import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.utility.Utility;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    static {
        try {
            LAST_SMS_HEARTBEAT_TIME = Utility.getCurrentDate();
            LAST_PREPARE_DATA_HEARTBEAT_TIME = Utility.getCurrentDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
