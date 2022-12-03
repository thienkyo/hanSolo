package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.BizReport;

public class BizReportResponse extends BaseResponse{
    private BizReport bizReport;

    public BizReport getBizReport() {
        return bizReport;
    }

    public void setBizReport(BizReport bizReport) {
        this.bizReport = bizReport;
    }

    public BizReportResponse(BizReport bizReport, String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        this.bizReport = bizReport;
    }
}
