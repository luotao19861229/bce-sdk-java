package com.baidubce.services.feed.model;

import com.baidubce.auth.BceCredentials;
import com.baidubce.model.AbstractBceRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by cdluoao1 on 2017/7/11.
 */
public class GetFeedRequest extends AbstractBceRequest {

    private final Log log = LogFactory.getLog(getClass());
    private String opUsername;
    private String opPassword;
    private String tgUsername;
    private String tgPassword;
    private String bceUser;
    private String tgSubname;
    @Override
    public GetFeedRequest withRequestCredentials(BceCredentials credentials) {
        this.setRequestCredentials(credentials);
        return this;
    }

    public String getOpUsername() {
        return opUsername;
    }

    public void setOpUsername(String opUsername) {
        this.opUsername = opUsername;
    }

    public String getOpPassword() {
        return opPassword;
    }

    public void setOpPassword(String opPassword) {
        this.opPassword = opPassword;
    }

    public String getTgUsername() {
        return tgUsername;
    }

    public void setTgUsername(String tgUsername) {
        this.tgUsername = tgUsername;
    }

    public String getTgPassword() {
        return tgPassword;
    }

    public void setTgPassword(String tgPassword) {
        this.tgPassword = tgPassword;
    }

    public String getBceUser() {
        return bceUser;
    }

    public void setBceUser(String bceUser) {
        this.bceUser = bceUser;
    }

    public String getTgSubname() {
        return tgSubname;
    }

    public void setTgSubname(String tgSubname) {
        this.tgSubname = tgSubname;
    }
}
