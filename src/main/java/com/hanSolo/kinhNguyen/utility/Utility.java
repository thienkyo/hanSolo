package com.hanSolo.kinhNguyen.utility;

import com.hanSolo.kinhNguyen.DTO.SmsStringResult;
import com.hanSolo.kinhNguyen.models.Coupon;
import com.hanSolo.kinhNguyen.models.DateTimeContainer;
import com.hanSolo.kinhNguyen.models.DatetimeWrapper;
import com.hanSolo.kinhNguyen.models.SmsJob;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Utility {

    final public static int AUTHENTICATION_TIMEOUT = 3 * 24 * 60 * 60 * 1000; // user stay logged in for 3days.

    final public static Boolean ACTIVE_STATUS = true;
    final public static Boolean INACTIVE_STATUS = false;

    final public static String HOME_BANNER = "HOMEBANNER";
    final public static String HOME_COLLECTION = "HOMECOLLECTION";

    final public static String LOGIN_DILIMITER = "d3m";
    final public static String DEFAULT_PW = "MTIzNDU2bHR0";//123456ltt
    final public static String SECRET_KEY = "OSOKDMNT009";
    final public static String SECRET_KEY_API_SMS = "kinhNguyenAPI";

    final public static String MEMBER_ROLE = "MEMBER";
    final public static String ADMIN_ROLE = "ADMIN";
    final public static String SUPERADMIN_ROLE = "SUPER_ADMIN";
    final public static String MOD_ROLE = "MOD";
    final public static String ACCOUNTANT_ROLE = "ACCOUNTANT";
    final public static String SUPER_ACCOUNTANT_ROLE = "SUPER_ACCOUNTANT";

    final public static String GODLIKE_ROLE = "GODLIKE";

    final public static String GODLIKE_CLIENTCODE = "GODLIKE";

    final public static String GROUP_CATEGORY = "CATEGORY";
    final public static String GROUP_COLLECTION = "COLLECTION";
    final public static String GROUP_BRANDING = "BRANDING";
    final public static String GROUP_TAGS = "TAG";

    final public static String SUCCESS_ERRORCODE = "SUCCESS";
    final public static String SUCCESS_MSG = "executed successfully";
    final public static String FAIL_ERRORCODE = "FAIL";
    final public static String FAIL_MSG = "fail";
    final public static String INSERT_SU_MSG = "INSERT_SU";
    final public static String UPDATE_SU_MSG = "UPDATE_SU";

 /*   final public static int ORDER_STATUS_ORDERED = 0;
    final public static int ORDER_STATUS_PAID = 1;
    final public static int ORDER_STATUS_SHIPPED = 2;
    final public static int ORDER_STATUS_DONE = 3;
    final public static int ORDER_USER_DELETE = 4;
    final public static int ORDER_SHOP_DELETE = 5;
    final public static int ORDER_NOT_BOOK = 6;*/

    // home page
    final public static int PRODUCT_PAGE_SIZE = 9;
    final public static int COLLECTION_PAGE_SIZE = 4;
    final public static int BLOG_PAGE_SIZE = 9;
    final public static int FIRST_TIME_LOAD_SIZE = 100;
    final public static String ALL = "ALL";

    final public static String SMS_QUEUE_INIT = "INIT";
    final public static String SMS_QUEUE_SENDING = "SENDING";
    final public static String SMS_QUEUE_SENT = "SENT";

    final public static String SMS_JOB_COMMON = "COMMON";
    final public static String SMS_JOB_SPECIFIC = "SPECIFIC";
    final public static String SMS_JOB_PARTICULAR = "PARTICULAR";
    final public static String SMS_JOB_FASTSMS = "FASTSMS";
    final public static String SMS_JOB_FASTSMS_PASSCODE = "1122";
    final public static String SMS_JOB_NOTIFYORDER = "NOTIFYORDER";
    final public static String SMS_JOB_LUCKYDRAW = "LUCKYDRAW";

    final public static String COUPON_TYPE_BILL = "BILL";
    final public static String COUPON_TYPE_FRAME = "FRAME";
    final public static String COUPON_TYPE_LENS = "LENS";
    final public static String COUPON_CREATED_BY_TOOL = "TOOL";
    final public static String COUPON_CREATED_BY_MANUAL = "MANUAL";

    final public static int LOGIN_MEMBER_LIST_SIZE = 40;
    final public static int CLIENT_SHOP_LIST_SIZE = 40;
    final public static int CLIENT_LIST_SIZE = 40;
    final public static int SMS_JOB_LIST_SIZE = 25;
    final public static int LENS_PRODUCT_LIST_SIZE = 30;
    final public static int ORDER_LIST_SIZE = 50;

    final public static String CLIENT_STATUS_INIT = "INIT";
    final public static String CLIENT_STATUS_ACTIVE = "ACTIVE";
    final public static String CLIENT_STATUS_INACTIVE = "INACTIVE";

    final public static String SHOP_STATUS_INIT = "INIT";
    final public static String SHOP_STATUS_ACTIVE = "ACTIVE";
    final public static String SHOP_STATUS_INACTIVE = "INACTIVE";

    final public static Date getFirstDateOfYear(String year) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return df.parse(year + "0101_000000");
    }

    final public static Date getLastDateOfYear(String year) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return df.parse(year + "1231_595900");
    }

    final public static Date getCurrentDate() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String currentTime = df.format(new Date());
        return df.parse(currentTime);
    }

    final public static Date getFirstDateOfMonth(String year, String month) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        month = month.length() > 1 ? month : "0" + month;
        return df.parse(year + month + "01_000000");
    }

    final public static Date getLastDateOfMonth(String year, String month) throws ParseException {
        Date firstDate = getFirstDateOfMonth(year, month);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDate);
        int res = calendar.getActualMaximum(Calendar.DATE);

        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        month = month.length() > 1 ? month : "0" + month;
        return df.parse(year + month + res + "_235959");
    }

    final public static DatetimeWrapper prepareMonthForScheduleTask() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        List<DateTimeContainer> dtContainer = new ArrayList<>();
        List<String> yearMonthList = new ArrayList<>();
        DateTimeContainer tempContainer;
        for(int i = 0; i < 3 ;i++){
            calendar.setTime(getCurrentDate());
            calendar.add(Calendar.MONTH, -i);
            String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            month = month.length() > 1 ? month : "0" + month;
            int year = calendar.get(Calendar.YEAR);
            tempContainer = new DateTimeContainer(String.valueOf(year), month,"00");
            dtContainer.add(tempContainer);
            yearMonthList.add(year + month);
        }

        Date startDate = Utility.getFirstDateOfMonth(dtContainer.get(dtContainer.size()-1).getYear(),
                dtContainer.get(dtContainer.size()-1).getMonth());
        return new DatetimeWrapper(dtContainer, startDate, Utility.getCurrentDate(), yearMonthList);
    }

    final public static ResponseEntity<String> savefile(String dir, MultipartFile uploadfile, String oldName) {
        HttpHeaders headers = new HttpHeaders();
        String oldFilepath = "";
        String filename = "empty";
        String filepath = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
            df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

            String currentTime = df.format(new java.util.Date());
            // Get the filename and build the local file path
            filename = currentTime + "-" + uploadfile.getOriginalFilename();
            if (!oldName.isEmpty()) {
                oldFilepath = Paths.get(dir, oldName).toString();
                try {
                    //Delete if tempFile exists
                    File fileTemp = new File(oldFilepath);
                    if (fileTemp.exists()) {
                        fileTemp.delete();
                    }
                } catch (Exception e) {
                    // if any error occurs
                    e.printStackTrace();
                }
            }

            //  String directory = env.getProperty("yoda.uploadedFiles.thumbnail");
            filepath = Paths.get(dir, filename).toString();

            // Save the file locally
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(uploadfile.getBytes());
            stream.close();

            headers.add("newName", filename);
            headers.add("imageDir", filepath);
            headers.setContentType(MediaType.TEXT_PLAIN);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(filename, headers, HttpStatus.OK);
    }


    final public static ResponseEntity<String> saveMultipleFile(String dir, List<MultipartFile> uploadFiles, String oldNames) {
        HttpHeaders headers = new HttpHeaders();
        String oldFilepath = "";
        StringBuilder fileNameStr = new StringBuilder();

        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
            df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

            uploadFiles.stream().forEach(file -> {
                byte[] bytes = new byte[0];
                try {
                    bytes = file.getBytes();
                    String currentTime = df.format(new java.util.Date());
                    String filename = currentTime + "_" + file.getOriginalFilename();
                    String filepath = Paths.get(dir, filename).toString();
                    Files.write(Paths.get(filepath), bytes);
                    if (fileNameStr.toString().isEmpty()) {
                        fileNameStr.append(filename);
                    } else {
                        fileNameStr.append("," + filename);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            if (!oldNames.isEmpty()) {
                // String[] parts = oldNames.split("|");
                List<String> oldNameList = Arrays.asList(oldNames.split(","));
                for (String oldName : oldNameList) {
                    oldFilepath = Paths.get(dir, oldName).toString();
                    try {
                        //Delete if tempFile exists
                        File fileTemp = new File(oldFilepath);
                        if (fileTemp.exists()) {
                            fileTemp.delete();
                        }
                    } catch (Exception e) {
                        // if any error occurs
                        e.printStackTrace();
                    }
                }
            }

            headers.add("newName", fileNameStr.toString());
            //headers.add("imageDir", filepath);
            headers.setContentType(MediaType.TEXT_PLAIN);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(fileNameStr.toString(), headers, HttpStatus.OK);
    }

    /**
     * ex: onlyAllowThisRole(request,Utility.SUPERADMIN_ROLE)
     * if true: allow continuing.
     * @param request
     * @param role
     * @return
     */
    final public static boolean onlyAllowThisRole(final HttpServletRequest request, String role){
        final Claims claims = (Claims) request.getAttribute("claims");
        if(((List<String>) claims.get("roles")).contains(role)){
            return true;
        }
        return false;
    }

    /**
     * validate coupon
     * @param couponList
     * @return
     */
    final public static List<Coupon> filterCoupon(List<Coupon> couponList){
        if ( couponList.isEmpty() ) {
            return couponList;
        }
        List<Coupon> result = new ArrayList<>();
        Date today = new Date();
        couponList.forEach(item->{
            Date expiredDate = new Date(item.getGmtModify().getTime() + (1000 * 60 * 60 * 24 * item.getLifespan().longValue()));
            if(today.before(expiredDate)){
                result.add(item);
            }
        });
        return result;
    }

    /**
     * for merge orderDetail in multiple editing
     * @param first
     * @param second
     * @param <T>
     * @return
     */
    public static <T> T mergeObjects(T first, T second){
        Class<?> clas = first.getClass();
        Field[] fields = clas.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value1 = field.get(first);
                Object value2 = field.get(second);

                if (value1 instanceof String || value2 instanceof String) {
                    String str1 = (value1 != null) ? (String) value1 : "";
                    String str2 = (value2 != null) ? (String) value2 : "";

                    String value = (str1.length() >= str2.length()) ? str1 : str2;
                    field.set(first, value);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) first;
    }

    public static <T> T mergeOrderDetail(T first, T second){
        Class<?> clas = first.getClass();
        Field[] fields = clas.getDeclaredFields();
        Object result = null;
        try {
            result = clas.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value1 = field.get(first);
                Object value2 = field.get(second);
                Object value = (value1 != null ) ? value1 : value2;
                field.set(result, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) result;
    }

    /**
     * convert string into map
     * ex: String value = {code = fn:random|6, name = att:name, year = fix:2022};
     * to map : [ "code":"fn:random|6", "name":"att:name", "year":"fix:2022" ]
     * fn: function, att: attribute, fix: fix value
     * @param value
     * @return
     */
    final public static Map<String, String> stringToMap( String value){
        value = value.substring(1, value.length()-1);           //remove curly brackets
        String[] keyValuePairs = value.split(",");        //split the string to create key-value pairs
        Map<String,String> map = new HashMap<>();

        for(String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split("=");                   //split the pairs to get key and value
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }
        return map;
    }

    /**
     * build sms content based on attribute
     * ex: String value = {code = fn:random|6, name = att:name, year = fix:2022};
     * to map : [ "code":"fn:random|6", "name":"att:name", "year":"fix:2022" ]
     * fn: function ex random, att: attribute of order, fix: fix value
     * @param job
     * @return
     */
    final public static SmsStringResult buildDynamicSms(SmsJob job){

        if(StringUtils.isEmpty(job.getAttList())){
            return new SmsStringResult(job.getMsgContentTemplate(),StringUtils.EMPTY) ;
        }

        Map<String, String> attMap = Utility.stringToMap(job.getAttList());

        if(attMap.isEmpty()){return new SmsStringResult(job.getMsgContentTemplate(),StringUtils.EMPTY);}

        String smsString = job.getMsgContentTemplate();
        SmsStringResult re = new SmsStringResult(smsString,StringUtils.EMPTY);
        for (Map.Entry<String, String> entry : attMap.entrySet()) {
            // entry.getValue() ex: fn:random|6
            // parts[0] is indicator ex: fn(call function), att(get name), fix : fix value
            // parts[1] is config ex: random|6
            String[] parts = entry.getValue().split(":");

            switch (parts[0]) {
                case "fn":
                    buildWithFunction(smsString, entry.getKey(), parts[1], re);
                    break;
                case "att":
                    break;
                case "fix":
                    buildWithFixValue(smsString, entry.getKey(), parts[1], re);
                    break;

                default:
                    return  re;
            }

        }

        return re;
    }

    private static SmsStringResult buildWithFixValue(String smsString, String position, String config, SmsStringResult re) {

        re.setSmsString(re.getSmsString().replaceAll("<"+position+">",config));
       // re.setExtendInfo(re.getExtendInfo()+"|"+config);

        //String sms = smsString.replaceAll("<"+position+">",config);
        return re;
    }

    /**
     *  @param smsString ex: coupon 40% code:<code>,ctrinh hang tuan tren FB
     * @param position ex: code
     * @param config ex: random|6
     * @param re
     */
    private static SmsStringResult buildWithFunction(String smsString, String position, String config, SmsStringResult re) {
        String[] functionAtt = config.split("\\|");
        String result;
        switch (functionAtt[0]) {
            case "random":
                result = RandomStringUtils.randomAlphanumeric(Integer.parseInt(functionAtt[1])).toUpperCase(Locale.ROOT);
                break;
            default:
                result =  "<"+position+">";
        }
        re.setSmsString(re.getSmsString().replaceAll("<"+position+">",result));
        re.setExtendInfo(result);
        return re;
    }


}
