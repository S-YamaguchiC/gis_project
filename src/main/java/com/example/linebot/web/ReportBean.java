package com.example.linebot.web;

public class ReportBean {

    /*
    * 値を保存しておくで
    * これをDBに入れるで
    * */
    private String lineId; //LINEid
    private int type;   //種別
    private int category;   //内容
    private String detail =""; //詳細
    private String latitude ="";
    private String longitude ="";
    private String accountId ="";


    public String flag;


    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setFlag(String flag) { this.flag = flag; }

    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getLineId() { return this.lineId; }

    public int getType(){ return this.type; }

    public int getCategory(){ return this.category; }

    public String getDetail(){ return this.detail; }

    public String getLatitude(){ return this.latitude; }

    public String getLongitude(){ return this.longitude; }

    public String getFlag() { return this.flag; }

    public String getAccountId() { return this.accountId; }

}
