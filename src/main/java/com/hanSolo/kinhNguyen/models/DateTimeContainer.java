package com.hanSolo.kinhNguyen.models;

public class DateTimeContainer {

    private String year = "0000";
    private String month = "00";
    private String day = "00";
    private String dateString;

    public DateTimeContainer(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dateString = year+month+day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        dateString = year+month+day;
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        dateString = year+month+day;
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        dateString = year+month+day;
        this.day = day;
    }

    public String getDateString() {
        return dateString;
    }

    @Override
    public boolean equals(Object obj) {
        DateTimeContainer container = (DateTimeContainer) obj;
        return this.getDateString().equals(container.getDateString());
    }

    @Override
    public String toString() {
        return "DateTimeContainer{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", dateString='" + dateString + '\'' +
                '}';
    }
}
