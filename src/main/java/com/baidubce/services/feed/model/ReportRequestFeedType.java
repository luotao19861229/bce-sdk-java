package com.baidubce.services.feed.model;


import java.util.Date;
import java.util.List;

/**
 * Created by cdluoao1 on 2017/7/13.
 */
public class ReportRequestFeedType {
    private String[] performanceData;
    private String startDate;
    private String endDate;
    private Boolean idOnly;
    private Integer levelOfDetails;
//    private List<AttributeType> attributes;
    private Integer format;
    private List<Long> statIds;
    private Integer statRange;
    private Integer reportType;
    private Integer unitOfTime;
    private Integer device;
//    private Boolean order;
//    private Integer number;
//    private Integer isrelativetime;
//    private Integer styleType;
    private Integer platform;
    private Integer subject;
    private Integer materialStyle;

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public String[] getPerformanceData() {
        return performanceData;
    }

    public void setPerformanceData(String[] performanceData) {
        this.performanceData = performanceData;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean getIdOnly() {
        return idOnly;
    }

    public void setIdOnly(Boolean idOnly) {
        this.idOnly = idOnly;
    }

    public Integer getLevelOfDetails() {
        return levelOfDetails;
    }

    public void setLevelOfDetails(Integer levelOfDetails) {
        this.levelOfDetails = levelOfDetails;
    }

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public List<Long> getStatIds() {
        return statIds;
    }

    public void setStatIds(List<Long> statIds) {
        this.statIds = statIds;
    }

    public Integer getStatRange() {
        return statRange;
    }

    public void setStatRange(Integer statRange) {
        this.statRange = statRange;
    }

    public Integer getUnitOfTime() {
        return unitOfTime;
    }

    public void setUnitOfTime(Integer unitOfTime) {
        this.unitOfTime = unitOfTime;
    }

    public Integer getDevice() {
        return device;
    }

    public void setDevice(Integer device) {
        this.device = device;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public Integer getMaterialStyle() {
        return materialStyle;
    }

    public void setMaterialStyle(Integer materialStyle) {
        this.materialStyle = materialStyle;
    }
}
