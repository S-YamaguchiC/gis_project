package com.example.linebot.dao;

import com.example.linebot.Bean.ReportBean;
import org.postgresql.geometric.PGcircle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
* 現行システムの " ContributionDAO.java" にあたるクラス
* */

@Repository
@Transactional
public class ReportDaoImpl extends AbstractDAO implements IReportDao {

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

    /**?
     * 画像のパスを保存して、そのときの contribution_id を次の insertContribution メソッドに渡す
     */
    @Override
    public boolean insertContributionImage(ReportBean report) {
        String sql = "insert into contribution_picture(contribution_id, picture_save_path) values (?,?)";
        if (jdbc.update(sql, report.getContribution_id(), report.getImagePath()) == 1) {
            return true;
        } else {
            return false;
        }
    }

    // 現行用
    /**?
     * 投稿の追加
     * ※genre : よく見たら文字列で検索してIDで入れてた...IDに変換してしまったので直で入れる
     * @param report    Beanクラス
     * @return
     */
    @Override
    public boolean insertContribution(ReportBean report) {
        String contributionInsertSql = "insert into contribution(genre_id, contribution_template_id, supplement, account_id, post_time, latlon, progress_id, available) "
                + "values(?, ?, ?, " +
                "(select account_id from line_auth where line_id = ?)," +
                " ?, ?, " +
                "(select progress_id from progress where progress = 'UNCONFIRMED'), ?)";

        String intsql = "select lastval()";

        if (jdbc.update(contributionInsertSql,
                report.getType(),
                report.getCategory(),
                report.getDetail(),
                report.getLineId(),
                report.getPostTime(),
                new PGcircle(report.getLongitude(), report.getLatitude(), 0d),
                true
        ) == 1) {
            // 直近の報告IDを取得
            report.setContribution_id(jdbc.queryForObject(intsql, Integer.class));
            return true;
        } else {
            return false;
        }
    }

    /**
     * lineidの存在を確認
     * */
    @Override
    public boolean existLineAccount(String lineId) {
        String sql = "select count(line_id) from line_auth "
                + "where line_id = ?";

        // booleanでいいかもしれない
        if (jdbc.queryForObject(sql, Integer.class, lineId) == 1){
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
