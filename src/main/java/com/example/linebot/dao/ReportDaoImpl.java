package com.example.linebot.dao;

import com.example.linebot.web.ReportBean;
import lombok.val;
import org.postgresql.geometric.PGcircle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;

/*
* 現行システムの " ContributionDAO.java" にあたるクラス
* */

@Repository
@Transactional
public class ReportDaoImpl extends AbstractDAO implements ReportDao {

    public static final String URL ="jdbc:postgresql://localhost:5432/postgres";
    public static final String USER_ID = "postgres";
    public static final String PASSWD = "postgres";


    private final JdbcTemplate jdbc;

    public ReportDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // テスト用データベース
    @Override
    public void insert(int type, int category, String detail, String latitude, String longitude)
            throws SQLException {

        String sql = "insert into test_db (type, category, detail, location_lat, location_lng) values (?,?,?,?,?)";

        try ( Connection conn = DriverManager.getConnection(URL, USER_ID, PASSWD) ) {

            try ( PreparedStatement ppst = conn.prepareStatement(sql) ) {

                ppst.setInt(1, type);
                ppst.setInt(2, category);
                ppst.setString(3, detail);
                ppst.setString(4, latitude);
                ppst.setString(5, longitude);

                ppst.executeUpdate();
            }
        }
    }


    // 現行用
    /**?
     * 投稿の追加
     * @param report    Beanクラス
     * @return
     */
    @Override
    public long insertContribution(ReportBean report) {
        long contributionId = 0;
        String contributionInsertSql = "insert into contribution(genre_id, contribution_template_id, supplement, account_id, post_time, latlon, progress_id, available) "
                + "values((select genre_id from genre where genre = :genre), "
                + "(select contribution_template_id from contribution_template where contribution_template = :contributionTemplate), "
                + ":supplement, :accountId , :postTime, :latlon, "
                + "(select progress_id from progress where progress = 'UNCONFIRMED'), :available) returning contribution_id";

        try (val conn = dbcp.getSql2o().open()) {
            contributionId = conn.createQuery(contributionInsertSql)
                    .addParameter("genre", report.getType())
                    .addParameter("supplement", report.getDetail())
                    .addParameter("contributionTemplate", report.getCategory())
                    .addParameter("accountId", report.getAccountId())
                    .addParameter("postTime", "")   // 投稿日時をどっかから
                    .addParameter("latlon", new PGcircle(Double.parseDouble(report.getLongitude()), Double.parseDouble(report.getLatitude()), 0d))
                    .addParameter("available", true)
                    .executeScalar(Long.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contributionId;
    }

    /**
     * lineidの存在を確認
     * */
    @Override
    public boolean existLineAccount(String lineId) {
        // ヤバそう
        String sql = "select count(line_id) from line_auth "
                + "where line_id = '" + lineId + "'";

        // booleanでいいかもしれない
        if (jdbc.queryForObject(sql, Integer.class) == 1){
            // IDが存在したとき
            return true;
        } else {
            // IDが存在しないとき
            return false;
        }
    }

    /**?
     * 投稿の前に、line_id をもとに account_id を取得する
     * @Param Id    新規作成のユーザID
     * @Param role  権限（管理者=1, 一般=2）
     * @return
     * */
    @Override
    public boolean insertAccountId(int Id, int role) {
        // account_id の最大値+1 を新規の account_id として設定、権限は2
        String sql = "insert into account(account_name, role_id) values(?,?)";
        if (jdbc.update(sql, Id, role) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * lineidの追加
     * @param Id
     * @param lineId
     * @return
     * */
    @Override
    public boolean registerLine(int Id, String lineId) {
        String sql = "insert into line_auth(account_id, line_id) " +
                "values((select account_id from account where account_name = ?), ?)";
        if (jdbc.update(sql, String.valueOf(Id), lineId) == 1) {
            // 追加成功
            return true;
        } else {
            // 追加失敗
            return false;
        }
    }

    /**?
     * 存在する account_id の最大値
     */
    @Override
    public int getMaxId() {
        String sql = "select max(account_id) from account";
        return jdbc.queryForObject(sql, Integer.class)+ 1;
    }

}
