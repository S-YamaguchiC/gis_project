package com.example.linebot.repository;

import com.example.linebot.dao.ReportDao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;

@Repository
@Transactional
public class ReportDaoImpl implements ReportDao {

    @Override
    public void insert(String type, String category, String detail, String latitude, String longitude)
            throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:postgresql:***********", // "jdbc:postgresql://[場所(Domain)]:[ポート番号]/[DB名]"
                "*******", // ログインロール
                "*******"); // パスワード
        Statement statement = connection.createStatement();

        System.out.println(type);
        System.out.println(category);
        System.out.println(detail);
        System.out.println(latitude);
        System.out.println(longitude);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("insert into test_db (type, category, detail, location_lat, location_lng) values('");
        stringBuilder.append(type);
        stringBuilder.append("','");
        stringBuilder.append(category);
        stringBuilder.append("','");
        stringBuilder.append(detail);
        stringBuilder.append("',");
        stringBuilder.append(latitude);
        stringBuilder.append(",");
        stringBuilder.append(longitude);
        stringBuilder.append(")");

        String sql = stringBuilder.toString();
        System.out.println(sql);

        statement.executeUpdate(sql);
    }
}
