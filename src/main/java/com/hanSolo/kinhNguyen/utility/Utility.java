package com.hanSolo.kinhNguyen.utility;

import io.jsonwebtoken.Claims;
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
    final public static int FIRTST_TIME_LOAD_SIZE = 100;

    final public static String SMS_QUEUE_INIT = "INIT";
    final public static String SMS_QUEUE_SENDING = "SENDING";
    final public static String SMS_QUEUE_SENT = "SENT";

    final public static String SMS_JOB_COMMON = "COMMON";
    final public static String SMS_JOB_SPECIFIC = "SPECIFIC";
    final public static String SMS_JOB_PARTICULAR = "PARTICULAR";
    final public static String SMS_JOB_FASTSMS = "FASTSMS";
    final public static String SMS_JOB_NOTIFYORDER = "NOTIFYORDER";
    final public static String SMS_JOB_FASTSMS_PASSCODE = "1122";

    final public static int LOGIN_MEMBER_LIST_SIZE = 40;
    final public static int CLIENT_SHOP_LIST_SIZE = 40;
    final public static int SMS_JOB_LIST_SIZE = 25;
    final public static int LENS_PRODUCT_LIST_SIZE = 30;


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
}
