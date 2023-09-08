package com.hanSolo.kinhNguyen.facade;

public interface ClientInterface {
    String getBrandName();
    String getPhone();
    String getAddress();
    String getClientCode();
    Boolean getIsUnlockSmsFeature();
    Integer getBizReportBeginMonthNumber();
    Integer getBizReportEndMonthNumber();
}
