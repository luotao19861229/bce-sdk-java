package com.baidubce.services.feed.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by cdluoao1 on 2017/7/12.
 * feed账号信息
 */
public class FeedAccount {
    private String accountName;
    private String password;
    private String bceUser;

    public FeedAccount(){

    }

    public FeedAccount(String accountName, String password){
        checkNotNull(accountName, "accountName should not be null.");
        checkArgument(!accountName.isEmpty(), "accountName should not be empty.");
        checkNotNull(password, "password should not be null.");
        checkArgument(!password.isEmpty(), "password should not be empty.");
        this.accountName=accountName;
        this.password=password;
    }
    public FeedAccount(String accountName, String password,String bceUser){
        checkNotNull(accountName, "accountName should not be null.");
        checkArgument(!accountName.isEmpty(), "accountName should not be empty.");
        checkNotNull(password, "password should not be null.");
        checkArgument(!password.isEmpty(), "password should not be empty.");
        checkNotNull(bceUser, "bceUser should not be null.");
        checkArgument(!bceUser.isEmpty(), "bceUser should not be empty.");
        this.accountName=accountName;
        this.password=password;
        this.bceUser=bceUser;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBceUser() {
        return bceUser;
    }

    public void setBceUser(String bceUser) {
        this.bceUser = bceUser;
    }
}
