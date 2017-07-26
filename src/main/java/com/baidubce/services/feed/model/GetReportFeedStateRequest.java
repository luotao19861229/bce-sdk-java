package com.baidubce.services.feed.model;

/**
 * Created by cdluotao1 on 2017/7/24.
 */
public class GetReportFeedStateRequest {

    public GetReportFeedStateRequest(String reportId){
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
