package com.greylabs.yoda.enums;

/**
 * Created by Jaybhay Vijay on 8/15/2015.
 */
public enum AccountType {
    LOCAL("local"),GOOGLE("com.google");
    private String accountType;
    AccountType(String accountType){
        this.accountType=accountType;
    }


    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public static AccountType getIntegerToEnum(int type){
        AccountType accountType=LOCAL;
        switch (type){
            case 0:accountType=LOCAL;break;
            case 1:accountType=GOOGLE;break;
        }
        return accountType;
    }
}
