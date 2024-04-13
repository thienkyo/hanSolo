package com.hanSolo.kinhNguyen.models;

import java.util.Date;

public class LastTimeContainer {
    private Date lastSmsHeartBeatTime;
    private Date lastPrepareDataHeartBeatTime;
    private Date lastCustomerSourceCalculationTime;
    private Date lastBizReportCalculationTime;

    public Date getLastSmsHeartBeatTime() {
        return lastSmsHeartBeatTime;
    }

    public void setLastSmsHeartBeatTime(Date lastSmsHeartBeatTime) {
        this.lastSmsHeartBeatTime = lastSmsHeartBeatTime;
    }

    public Date getLastPrepareDataHeartBeatTime() {
        return lastPrepareDataHeartBeatTime;
    }

    public void setLastPrepareDataHeartBeatTime(Date lastPrepareDataHeartBeatTime) {
        this.lastPrepareDataHeartBeatTime = lastPrepareDataHeartBeatTime;
    }

    public Date getLastCustomerSourceCalculationTime() {
        return lastCustomerSourceCalculationTime;
    }

    public void setLastCustomerSourceCalculationTime(Date lastCustomerSourceCalculationTime) {
        this.lastCustomerSourceCalculationTime = lastCustomerSourceCalculationTime;
    }

    public Date getLastBizReportCalculationTime() {
        return lastBizReportCalculationTime;
    }

    public void setLastBizReportCalculationTime(Date lastBizReportCalculationTime) {
        this.lastBizReportCalculationTime = lastBizReportCalculationTime;
    }
}
