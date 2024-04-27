package com.hanSolo.kinhNguyen.service;

import com.hanSolo.kinhNguyen.models.BizReport;
import com.hanSolo.kinhNguyen.models.DateTimeContainer;
import com.hanSolo.kinhNguyen.models.DatetimeWrapper;
import com.hanSolo.kinhNguyen.repository.BizReportRepository;
import com.hanSolo.kinhNguyen.repository.ClientRepository;
import com.hanSolo.kinhNguyen.repository.ShopRepository;
import com.hanSolo.kinhNguyen.repository.SmsJobRepository;
import com.hanSolo.kinhNguyen.repository.SmsQueueRepository;
import com.hanSolo.kinhNguyen.repository.SmsUserInfoRepository;
import com.hanSolo.kinhNguyen.repository.SpecificSmsUserInfoRepository;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
    @Autowired private SmsService smsService;
    @Autowired private CustomerSourceReportService customerSourceReportService;

    /**
     * build data for sms function
     * run every 4h every day
     * first run 00:30:35
     * run at 00 04 08 12 16 20
     * @throws ParseException
     */
    @Scheduled(cron = "35 30 0/4 * * *")
    //@Scheduled(cron = "15 * * * * ?")
    public void schedulePrepareSmsData() throws ParseException {
        smsService.prepareSmsData();
        LOGGER.info("prepare sms data every 4hrs");
    }

    /**
     * calculate for the last 3 months
     * At 25 seconds past the minute,
     * at 1 minutes past the hour, every 3 hours,
     * between 07:00 AM and 07:59 PM
     * @throws ParseException
     */
    @Scheduled(cron = "25 1 7-19/3 * * *")
    //@Scheduled(cron = "25 1 0/6 * * *")
    //@Scheduled(cron = "*/5 * * * * *")
    public void scheduleCalculation() throws ParseException {
        // new logic here
        Calendar calendar = Calendar.getInstance();
        List<DateTimeContainer> dtContainer = new ArrayList<>();
        List<String> yearMonthList = new ArrayList<>();
        DateTimeContainer tempContainer;
        for(int i = 0; i < 3 ;i++){
            calendar.setTime(Utility.getCurrentDate());
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
        DatetimeWrapper dtWrapper = new DatetimeWrapper(dtContainer, startDate, Utility.getCurrentDate(), yearMonthList);

        LOGGER.info("scheduleCalculation, DatetimeWrapper:" + dtWrapper);

        // calculate customer source report.
        customerSourceReportService.calCustomerSourceReportForScheduleTask(dtWrapper);

        // calculate biz report
        bizReportService.calculateReportForScheduleTask(dtWrapper);

    }


    /**
     * once a month
     * At 03:05 AM, on day 1 of the month
     */
    //@Scheduled(cron = "0 5 3 1 * *")
    //@Scheduled(cron = "*/5 * * * * *")
    public void monthlyTask() {

    }

}
