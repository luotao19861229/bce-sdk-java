package com.baidubce.services.feed.model;

import com.baidubce.model.AbstractBceResponse;

/**
 * Created by cdluoao1 on 2017/7/11.
 */
public class GetFeedResponse extends AbstractBceResponse {

    private String reportId;

    private String isGenerated;

    private String reportFilePath;

    private String fileId;

    private String filePaths;


    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getIsGenerated() {
        return isGenerated;
    }

    public void setIsGenerated(String isGenerated) {
        this.isGenerated = isGenerated;
    }

    public String getReportFilePath() {
        return reportFilePath;
    }

    public void setReportFilePath(String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(String filePaths) {
        this.filePaths = filePaths;
    }
}
