package com.hanSolo.kinhNguyen.service;

import com.hanSolo.kinhNguyen.cacheCenter.CommonCache;
import com.hanSolo.kinhNguyen.models.BizReport;
import com.hanSolo.kinhNguyen.models.Shop;
import com.hanSolo.kinhNguyen.models.SmsJob;
import com.hanSolo.kinhNguyen.models.SmsQueue;
import com.hanSolo.kinhNguyen.models.SmsUserInfo;
import com.hanSolo.kinhNguyen.models.SpecificSmsUserInfo;
import com.hanSolo.kinhNguyen.repository.BizReportRepository;
import com.hanSolo.kinhNguyen.repository.ClientRepository;
import com.hanSolo.kinhNguyen.repository.ShopRepository;
import com.hanSolo.kinhNguyen.repository.SmsJobRepository;
import com.hanSolo.kinhNguyen.repository.SmsQueueRepository;
import com.hanSolo.kinhNguyen.repository.SmsUserInfoRepository;
import com.hanSolo.kinhNguyen.repository.SpecificSmsUserInfoRepository;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Component
public class ScheduledTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired private SmsQueueRepository smsQueueRepo;
    @Autowired private SmsUserInfoRepository smsUserInfoRepo;
    @Autowired private SmsJobRepository smsJobRepo;
    @Autowired private SpecificSmsUserInfoRepository specificSmsUserInfoRepo;
    @Autowired private ClientRepository clientRepo;
    @Autowired private ShopRepository shopRepo;
    @Autowired private BizReportRepository bizReportRepo;

    @Autowired private BizReportService bizReportService;

    /**
     * build data for sms function
     * first run 00:30:35
     * run at 00 04 08 12 16 20
     * @throws ParseException
     */
    @Scheduled(cron = "35 30 0/4 * * *")
    //@Scheduled(cron = "15 * * * * ?")
    public void schedulePrepareSmsData() throws ParseException {
        if(!CommonCache.SMS_DATA_PREPARE_CONTROL){
            return ;
        }

        List<SmsJob> smsJobList = smsJobRepo.findByStatus(true);
        List<SmsQueue> smsQueueList = new ArrayList<>();
        for (SmsJob job : smsJobList) {
            Calendar calendarCreateOrder = Calendar.getInstance();
            Calendar calendarNoSmsDay = Calendar.getInstance();
            if(job.getIntervalTime() >= 1){
                calendarCreateOrder.add(Calendar.DAY_OF_MONTH, -job.getIntervalTime());
            }
            calendarNoSmsDay.add(Calendar.DAY_OF_MONTH, -job.getNoSmsDays());

            if(Utility.SMS_JOB_COMMON.equals(job.getJobType())){// for common case: apply to all user
                List<SmsUserInfo> smsUserInfos;
                if(null != job.getStartDate()){
                    smsUserInfos = job.getIsTest() ?
                            smsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndIsTestUserAndOrderCreateDateAfterOrderByOrderCreateDateAsc(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%",calendarNoSmsDay.getTime(),true,job.getStartDate())
                            : smsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndOrderCreateDateAfterOrderByOrderCreateDateAsc(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%", calendarNoSmsDay.getTime(), job.getStartDate());
                }else{
                    smsUserInfos = job.getIsTest() ?
                            smsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndIsTestUser(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%",calendarNoSmsDay.getTime(),true)
                            : smsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBefore(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%", calendarNoSmsDay.getTime());
                }

                for (SmsUserInfo smsUserInfo : smsUserInfos) {
                    smsUserInfo.setJobIdList(smsUserInfo.getJobIdList() + "," + job.getId() + "|");
                    smsUserInfo.setLastSendSmsDate(Utility.getCurrentDate());

                    SmsQueue smsQueue = generateSmsQueue(job,smsUserInfo);
                    smsQueueList.add(smsQueue);
                }
                smsUserInfoRepo.saveAll(smsUserInfos);
            }else if(Utility.SMS_JOB_SPECIFIC.equals(job.getJobType())){ // for recheck case
                List<SpecificSmsUserInfo> specSmsUserInfos = job.getIsTest() ?
                        specificSmsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndJobIdToRunAndIsTestUser(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%",calendarNoSmsDay.getTime(),job.getId().toString(),true)
                        : specificSmsUserInfoRepo.findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndJobIdToRun(calendarCreateOrder.getTime(),"%,"+job.getId().toString() +"|%", calendarNoSmsDay.getTime(),job.getId().toString());

                for (SpecificSmsUserInfo specificSmsUserInfo : specSmsUserInfos) {
                    specificSmsUserInfo.setJobIdList(specificSmsUserInfo.getJobIdList() + "," + job.getId() + "|");
                    specificSmsUserInfo.setLastSendSmsDate(Utility.getCurrentDate());

                    SmsQueue smsQueue = generateSmsQueue2(job,specificSmsUserInfo);
                    smsQueueList.add(smsQueue);
                }
                specificSmsUserInfoRepo.saveAll(specSmsUserInfos);
            }

            if(Utility.SMS_JOB_PARTICULAR.equals(job.getJobType())){ // for Specific Phones field
                if(!job.getSpecificPhones().isEmpty()){
                    List<String> phones = Arrays.asList(job.getSpecificPhones().split("\\s*,\\s*"));
                    for(String phone : phones){
                        SmsQueue smsQueue = generateSmsQueue3(job,phone);
                        smsQueueList.add(smsQueue);
                    }
                    job.setSpecificPhones("");
                    smsJobRepo.save(job);
                }
            }
        }

        smsQueueRepo.saveAll(smsQueueList);
        CommonCache.LAST_PREPARE_DATA_HEARTBEAT_TIME = Utility.getCurrentDate();
        LOGGER.info("prepare sms data every 4hrs");
    }

    private SmsQueue generateSmsQueue(SmsJob job, SmsUserInfo smsUserInfo) throws ParseException {
        SmsQueue smsQueue = new SmsQueue();
        smsQueue.setJobId(job.getId());
        smsQueue.setGmtCreate(Utility.getCurrentDate());
        smsQueue.setGmtModify(Utility.getCurrentDate());
        smsQueue.setContent(job.getMsgContentTemplate() +" ["+ RandomStringUtils.randomAlphanumeric(4)+"]");
        smsQueue.setGender(smsUserInfo.getGender());
        smsQueue.setStatus(Utility.SMS_QUEUE_INIT);
        smsQueue.setReceiverName(smsUserInfo.getName());
        smsQueue.setReceiverPhone(smsUserInfo.getPhone());
        smsQueue.setWeight(job.getWeight());
        smsQueue.setJobType(job.getJobType());
        smsQueue.setClientCode(job.getClientCode());
        smsQueue.setShopCode(job.getShopCode());

        return smsQueue;
    }

    private SmsQueue generateSmsQueue2(SmsJob job, SpecificSmsUserInfo smsUserInfo) throws ParseException {
        SmsQueue smsQueue = new SmsQueue();
        smsQueue.setJobId(job.getId());
        smsQueue.setGmtCreate(Utility.getCurrentDate());
        smsQueue.setGmtModify(Utility.getCurrentDate());
        smsQueue.setContent(job.getMsgContentTemplate() +" ["+ RandomStringUtils.randomAlphanumeric(4)+"]");
        smsQueue.setGender(smsUserInfo.getGender());
        smsQueue.setStatus(Utility.SMS_QUEUE_INIT);
        smsQueue.setReceiverName(smsUserInfo.getName());
        smsQueue.setReceiverPhone(smsUserInfo.getPhone());
        smsQueue.setWeight(job.getWeight());
        smsQueue.setJobType(job.getJobType());
        smsQueue.setClientCode(job.getClientCode());
        smsQueue.setShopCode(job.getShopCode());

        return smsQueue;
    }

    private SmsQueue generateSmsQueue3(SmsJob job, String phone) throws ParseException {
        SmsQueue smsQueue = new SmsQueue();
        smsQueue.setJobId(job.getId());
        smsQueue.setGmtCreate(Utility.getCurrentDate());
        smsQueue.setGmtModify(Utility.getCurrentDate());
        smsQueue.setContent(job.getMsgContentTemplate() +" ["+ RandomStringUtils.randomAlphanumeric(4)+"]");
        smsQueue.setStatus(Utility.SMS_QUEUE_INIT);
        smsQueue.setReceiverPhone(phone);
        smsQueue.setWeight(job.getWeight());
        smsQueue.setJobType(job.getJobType());
        smsQueue.setClientCode(job.getClientCode());
        smsQueue.setShopCode(job.getShopCode());

        return smsQueue;
    }

    /**
     * build data for sms function
     * run every 6h
     * first run at 00:01:25
     * @throws ParseException
     */
    @Scheduled(cron = "25 1 0/6 * * *")
    //@Scheduled(cron = "*/5 * * * * *")
    public void scheduleExpenseCalculation() throws ParseException {

        Calendar calendar = Calendar.getInstance();

        // container only used as a place to hold year, month
        List<BizReport> container = new ArrayList<>();
        BizReport tempHolder;
        for(int i = 0; i<3 ;i++){
            tempHolder = new BizReport();
            calendar.setTime(Utility.getCurrentDate());
            calendar.add(Calendar.MONTH, -i);
            //int month = calendar.get(Calendar.MONTH) + 1;
            String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            month = month.length() > 1 ? month : "0" + month;
            int year = calendar.get(Calendar.YEAR);
            tempHolder.setYear(String.valueOf(year));
            tempHolder.setMonth(month);
            container.add(tempHolder);
        }

        LOGGER.info("expense calculation every 6hrs: year/month container size :" + container.size());

        List<BizReport> bizReportList = new ArrayList<>();
        for(BizReport br : container){
            List<BizReport> tempList = bizReportRepo.findByYearAndMonth(br.getYear(), br.getMonth());
            bizReportList.addAll(tempList);
        }

        LOGGER.info("expense calculation every 6hrs: year/month bizReportList size :" + bizReportList.size());

        for(BizReport br : bizReportList){
            bizReportService.calculateReport(br);
        }
    }

}
