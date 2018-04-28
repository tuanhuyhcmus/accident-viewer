/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis.api.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import thesis.api.common.AppConst;
import thesis.api.common.DefineName;
import thesis.api.data.History;
import thesis.api.database.MySqlFactory;

/**
 *
 * @author huynct
 */
public class AccidentModel {

    private static AccidentModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());

    public static AccidentModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new AccidentModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public int getAllActiveAccident(List<History> listActiveAccident) {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        History history;
        boolean isSuccess = true;
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr = String.format("SELECT `history_id` FROM %s ", DefineName.SESSION_TABLE_NAME);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null) {
                while (rs.next()) {
                    history = new History();
                    ret = getIncidentById(rs.getLong("history_id"), history, AppConst.TYPE_ACCIDENT);
                    if (ret == 0) {
                        listActiveAccident.add(history);
                    } else {
                        isSuccess = false;
                        ret = -1;
                        break;
                    }
                }
                if (isSuccess) {
                    ret = 0;
                }
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".getAllActiveAccident: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;

    }

    private int getIncidentById(long historyId, History existedHistory, int type) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr = String.format("SELECT * FROM %s WHERE `history_id` = %d AND `type`=%d", DefineName.HISTORY_TABLE_NAME, historyId, type);
            stmt.executeQuery(queryStr);
            rs = stmt.getResultSet();

            if (rs != null) {
                if (rs.next()) {
                    existedHistory.setHistoryId(rs.getLong("history_id"));
                    existedHistory.setAccident_number(rs.getLong("accident_number"));
                    existedHistory.setDislike(rs.getInt("dislike"));
                    existedHistory.setEndTime(rs.getString("end_time"));
                    existedHistory.setLat(rs.getDouble("lat"));
                    existedHistory.setLng(rs.getDouble("lng"));
                    if (!HistoryModel.getInstance().getListLinkImageByHistoryId(rs.getLong("history_id")).equals("")) {
                        existedHistory.setListImageLink(HistoryModel.getInstance().getListLinkImageByHistoryId(rs.getLong("history_id")));
                    }
                    existedHistory.setPostTime(rs.getString("post_time"));
                    existedHistory.setTextInfo(rs.getString("text_info"));
                    existedHistory.setUserId(rs.getLong("user_id"));
                    existedHistory.setType(rs.getInt("type"));
                    ret = 0;
                }
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".getAccidentById: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int getListAccdientByUserId(long uid, List<History> listExistedHistory) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr = String.format("SELECT * FROM %s WHERE `user_id` = %d AND `history_id`=`accident_number`", DefineName.HISTORY_TABLE_NAME, uid);
            stmt.executeQuery(queryStr);
            rs = stmt.getResultSet();

            if (rs != null) {
                if (rs.next()) {
                    History existedHistory = new History();
                    existedHistory.setHistoryId(rs.getLong("history_id"));
                    existedHistory.setAccident_number(rs.getLong("accident_number"));
                    existedHistory.setDislike(rs.getInt("dislike"));
                    existedHistory.setEndTime(rs.getString("end_time"));
                    existedHistory.setLat(rs.getDouble("lat"));
                    existedHistory.setLng(rs.getDouble("lng"));
                    existedHistory.setListImageLink(HistoryModel.getInstance().getListLinkImageByHistoryId(rs.getLong("history_id")));
                    existedHistory.setPostTime(rs.getString("post_time"));
                    existedHistory.setTextInfo(rs.getString("text_info"));
                    existedHistory.setUserId(rs.getLong("user_id"));
                    existedHistory.setType(rs.getInt("type"));
                    listExistedHistory.add(existedHistory);
                }
                ret = 0;
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".getAccidentById: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int getAllActiveTraffic(List<History> listActiveTraffic) {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        History history;
        boolean isSuccess = true;
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr = String.format("SELECT `history_id` FROM %s ", DefineName.SESSION_TABLE_NAME);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null) {
                while (rs.next()) {
                    history = new History();
                    ret = getIncidentById(rs.getLong("history_id"), history, AppConst.TYPE_TRAFFIC);
                    if (ret == 0) {
                        listActiveTraffic.add(history);
                    }
                }
                if (isSuccess) {
                    ret = 0;
                }
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".getAllActiveTraffic: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }
}
