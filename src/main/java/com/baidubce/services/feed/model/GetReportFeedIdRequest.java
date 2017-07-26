package com.baidubce.services.feed.model;

/**
 * Created by cdluotao1 on 2017/7/24.
 */
public class GetReportFeedIdRequest {
    private ReportRequestFeedType reportRequestType;

    public ReportRequestFeedType getReportRequestType() {
        return reportRequestType;
    }

    public void setReportRequestType(ReportRequestFeedType reportRequestType) {
        this.reportRequestType = reportRequestType;
    }
}
