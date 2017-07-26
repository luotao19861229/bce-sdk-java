package com.baidubce.services.feed.model;

import java.util.List;

/**
 * Created by cdluoao1 on 2017/7/13.
 */
public class GetAllFeedObjectsRequest {
    private List<Long> campaignIds;
    private Integer format;
    private List<String> feedAccountFields;
    private List<String> feedCampaignFields;
    private List<String> feedAdgroupFields;
    private List<String> feedCreativeFields;

    public List<Long> getCampaignIds() {
        return campaignIds;
    }

    public void setCampaignIds(List<Long> campaignIds) {
        this.campaignIds = campaignIds;
    }

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public List<String> getFeedAccountFields() {
        return feedAccountFields;
    }

    public void setFeedAccountFields(List<String> feedAccountFields) {
        this.feedAccountFields = feedAccountFields;
    }

    public List<String> getFeedCampaignFields() {
        return feedCampaignFields;
    }

    public void setFeedCampaignFields(List<String> feedCampaignFields) {
        this.feedCampaignFields = feedCampaignFields;
    }

    public List<String> getFeedAdgroupFields() {
        return feedAdgroupFields;
    }

    public void setFeedAdgroupFields(List<String> feedAdgroupFields) {
        this.feedAdgroupFields = feedAdgroupFields;
    }

    public List<String> getFeedCreativeFields() {
        return feedCreativeFields;
    }

    public void setFeedCreativeFields(List<String> feedCreativeFields) {
        this.feedCreativeFields = feedCreativeFields;
    }
}
