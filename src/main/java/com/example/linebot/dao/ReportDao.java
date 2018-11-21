package com.example.linebot.dao;

import java.sql.SQLException;

public interface ReportDao {

    //登録(画像なし)
    void insert(int type, int category, String detail, String latitude, String longitude) throws SQLException;
}
