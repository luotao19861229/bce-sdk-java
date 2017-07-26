package com.baidubce.services.feed.model;

/**
 * Created by cdluoao1 on 2017/7/24.
 */
public class GetReportFeedFileUrlRequest {

    public GetReportFeedFileUrlRequest(String reportId){
        this.reportId=reportId;
    }
    private String reportId;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

}
