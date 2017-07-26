package com.baidubce.services.feed.model;

import com.baidubce.model.AbstractBceResponse;

/**
 * Created by cdluoao1 on 2017/7/13.
 */
public class FilePathFeedTypeResponse extends AbstractBceResponse {
    private String feedAccountFilePath;
    private String feedCampaignFilePath;
    private String feedAdgroupFilePath;
    private String feedCreativeFilePath;
    private String feedAccountMd5;
    private String feedCampaigMd5;
    private String feedAdgroupMd5;
    private String feedCreativeMd5;

    public FilePathFeedTypeResponse(){

    }
    public void copyValue(FilePathFeedTypeResponse filePathFeedTypeResponse){
        this.feedAccountFilePath=filePathFeedTypeResponse.getFeedAccountFilePath();
        this.feedCampaignFilePath=filePathFeedTypeResponse.getFeedCampaignFilePath();
        this.feedAdgroupFilePath=filePathFeedTypeResponse.getFeedAdgroupFilePath();
        this.feedCreativeFilePath=filePathFeedTypeResponse.getFeedCreativeFilePath();
        this.feedAccountMd5=filePathFeedTypeResponse.getFeedAccountMd5();
        this.feedCampaigMd5=filePathFeedTypeResponse.getFeedCampaigMd5();
        this.feedAdgroupMd5=filePathFeedTypeResponse.getFeedAdgroupMd5();
        this.feedCreativeMd5=filePathFeedTypeResponse.getFeedCreativeMd5();
    }

    public String getFeedAccountFilePath() {
        return feedAccountFilePath;
    }

    public void setFeedAccountFilePath(String feedAccountFilePath) {
        this.feedAccountFilePath = feedAccountFilePath;
    }

    public String getFeedCampaignFilePath() {
        return feedCampaignFilePath;
    }

    public void setFeedCampaignFilePath(String feedCampaignFilePath) {
        this.feedCampaignFilePath = feedCampaignFilePath;
    }

    public String getFeedAdgroupFilePath() {
        return feedAdgroupFilePath;
    }

    public void setFeedAdgroupFilePath(String feedAdgroupFilePath) {
        this.feedAdgroupFilePath = feedAdgroupFilePath;
    }

    public String getFeedCreativeFilePath() {
        return feedCreativeFilePath;
    }

    public void setFeedCreativeFilePath(String feedCreativeFilePath) {
        this.feedCreativeFilePath = feedCreativeFilePath;
    }

    public String getFeedAccountMd5() {
        return feedAccountMd5;
    }

    public void setFeedAccountMd5(String feedAccountMd5) {
        this.feedAccountMd5 = feedAccountMd5;
    }

    public String getFeedCampaigMd5() {
        return feedCampaigMd5;
    }

    public void setFeedCampaigMd5(String feedCampaigMd5) {
        this.feedCampaigMd5 = feedCampaigMd5;
    }

    public String getFeedAdgroupMd5() {
        return feedAdgroupMd5;
    }

    public void setFeedAdgroupMd5(String feedAdgroupMd5) {
        this.feedAdgroupMd5 = feedAdgroupMd5;
    }

    public String getFeedCreativeMd5() {
        return feedCreativeMd5;
    }

    public void setFeedCreativeMd5(String feedCreativeMd5) {
        this.feedCreativeMd5 = feedCreativeMd5;
    }
}
