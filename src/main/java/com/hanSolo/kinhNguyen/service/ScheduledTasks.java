package com.hanSolo.kinhNguyen.service;

import com.hanSolo.kinhNguyen.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class ScheduledTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
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
     * at 1 minutes past the hour, every 1 hours,
     * between 07:00 AM and 07:59 PM
     * server time 7hrs behind.
     * @throws ParseException
     */
    @Scheduled(cron = "25 1 0-14/1 * * *")
    //@Scheduled(cron = "25 1 0/6 * * *")
    //@Scheduled(cron = "*/5 * * * * *")
    public void scheduleCalculation() throws ParseException {
        LOGGER.info("scheduleCalculation, DatetimeWrapper:" + Utility.prepareMonthForScheduleTask());

        // calculate customer source report.
        customerSourceReportService.calCustomerSourceReportForScheduleTask(Utility.prepareMonthForScheduleTask());

        // calculate biz report
        bizReportService.calculateReportForScheduleTask(Utility.prepareMonthForScheduleTask());
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
