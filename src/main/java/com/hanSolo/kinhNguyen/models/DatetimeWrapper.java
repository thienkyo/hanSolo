package com.hanSolo.kinhNguyen.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatetimeWrapper {
    private List<DateTimeContainer> dateTimeList;

    private List<String> yearMonthStrList = new ArrayList<>();

    private Date startTime;

    private Date endTime;

    public DatetimeWrapper(List<DateTimeContainer> dateTimeList, Date startTime, Date endTime, List<String> yearMonthStrList) {
        this.dateTimeList = dateTimeList;
        this.startTime = startTime;
        this.endTime = endTime;
        this.yearMonthStrList =  yearMonthStrList;
    }

    public List<DateTimeContainer> getDateTimeList() {
        return dateTimeList;
    }

    public void setDateTimeList(List<DateTimeContainer> dateTimeList) {
        this.dateTimeList = dateTimeList;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<String> getYearMonthStrList() {
        return yearMonthStrList;
    }

    @Override
    public String toString() {
        return "DatetimeWrapper{" +
                "dateTimeList=" + dateTimeList +
                ", yearMonthStrList=" + yearMonthStrList +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
