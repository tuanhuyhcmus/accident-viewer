/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis.api.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import thesis.api.common.AppConst;
import thesis.api.common.CommonFunction;
import thesis.api.common.DefineName;
import thesis.api.data.History;
import thesis.api.database.MySqlFactory;

/**
 *
 * @author huynct
 */
public class HistoryModel {

    private static HistoryModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());

    public static HistoryModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new HistoryModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public int insertHistory(History history) {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int ret = -1;

        try {

            connection = MySqlFactory.getConnection();
            connection.setAutoCommit(false);

            String queryStr;

            String historyTableName = DefineName.HISTORY_TABLE_NAME;

            queryStr = String.format("INSERT INTO %s (`user_id`,`post_time`,`text_info`,`lat`,`lng`,`end_time`,`type` ) VALUES(%d,'%s','%s',%f,%f,'%s',%d) ", historyTableName, history.getUserId(), CommonFunction.getCurrentDateTime(),
                    history.getTextInfo(),
                    history.getLat(), history.getLng(),
                    CommonFunction.getCurrentDateTime(CommonFunction.getCurrentTimeMillis() + AppConst.TIME_TO_END_ACCDIENT),
                    history.getType()
            );
            stmt = connection.prepareStatement(queryStr, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            boolean isSuccess = true;
            if (rs.next()) {

                history.setHistoryId(rs.getLong(1));

                queryStr = String.format("UPDATE %s SET `accident_number` = %d WHERE `history_id`=%d", DefineName.HISTORY_TABLE_NAME, history.getHistoryId(), history.getHistoryId());
                stmt = connection.prepareStatement(queryStr);
                if (stmt.executeUpdate() > 0) {
                    String[] listImage = null;
                    if (history.getListImageLink().contains(",")) {
                        listImage = history.getListImageLink().split(",");
                    }
                    if (listImage!=null) {
                        for (int i = 0; i < listImage.length; i++) {
                            queryStr = String.format("INSERT INTO %s (accident_id,image_link) VALUES(%d,'%s')", DefineName.ACCIDENT_IMAGE_TABLE_NAME, history.getHistoryId(), listImage[i]);
                            stmt = connection.prepareStatement(queryStr);
                            if (stmt.executeUpdate() <= 0) {
                                isSuccess = false;
                                break;
                            }
                        }
                    }
                    if (isSuccess) {
                        connection.commit();
                        ret = 0;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".insertHistory: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int mergeInfoWithExistedHistory(AtomicLong existedHistoryId, History history, History existedHistory) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int ret = -1;

        try {

            connection = MySqlFactory.getConnection();
            connection.setAutoCommit(false);
            String queryStr;
            String historyTableName = DefineName.HISTORY_TABLE_NAME;
            queryStr = String.format("INSERT INTO %s (`user_id`,`post_time`,`text_info`,`lat`,`lng`,`accident_number`,`type` ) VALUES(%d,'%s','%s',%f,%f,%d,%d) ", historyTableName, history.getUserId(), CommonFunction.getCurrentDateTime(),
                    history.getTextInfo(),
                    history.getLat(), history.getLng(),
                    existedHistoryId.get(),
                    history.getType()
            );
            stmt = connection.prepareStatement(queryStr);

            if (stmt.executeUpdate() > 0) {

                queryStr = String.format("SELECT * FROM %s WHERE `history_id`=%d", DefineName.HISTORY_TABLE_NAME, existedHistoryId.get());
                stmt = connection.prepareStatement(queryStr);
                stmt.execute();
                rs = stmt.getResultSet();
                if (rs != null) {
                    if (rs.next()) {
                        StringBuilder newTextInfo = new StringBuilder();
                        newTextInfo.append(rs.getString("text_info"));
                        if (!rs.getString("text_info").equals("")) {
                            newTextInfo.append(DefineName.SPLIT_TEXT_INFO_STRING);
                        }
                        newTextInfo.append(history.getTextInfo());

                        existedHistory.setHistoryId(rs.getLong("history_id"));
                        existedHistory.setAccident_number(rs.getLong("accident_number"));
                        existedHistory.setDislike(rs.getInt("dislike"));
                        existedHistory.setEndTime(rs.getString("end_time"));
                        existedHistory.setLat(rs.getDouble("lat"));
                        existedHistory.setLng(rs.getDouble("lng"));

                        String oldListHistory = getListLinkImageByHistoryId(rs.getLong("history_id"));

                        if (!oldListHistory.equals("")) {
                            if (!history.getListImageLink().equals("")) {
                                existedHistory.setListImageLink(oldListHistory + DefineName.SPLIT_LINK_IMAGE_STRING + history.getListImageLink());
                            } else {
                                existedHistory.setListImageLink(oldListHistory);
                            }
                        } else {
                            existedHistory.setListImageLink(newTextInfo.toString());
                        }
                        existedHistory.setPostTime(rs.getString("post_time"));
                        existedHistory.setTextInfo(newTextInfo.toString());
                        existedHistory.setUserId(rs.getLong("user_id"));
                        existedHistory.setType(rs.getInt("type"));
                        queryStr = String.format("UPDATE %s SET `text_info`='%s' ,`like`=`like`+1 WHERE `history_id`=%d", DefineName.HISTORY_TABLE_NAME, newTextInfo, existedHistoryId.get());
                        stmt = connection.prepareStatement(queryStr);
                        if (stmt.executeUpdate() > 0) {

                            String[] listImage = null;
                            if (history.getListImageLink().contains(",")) {
                                listImage = history.getListImageLink().split(",");
                            }
                            boolean isSuccess = true;
                            if (listImage != null) {
                                for (int i = 0; i < listImage.length; i++) {
                                    queryStr = String.format("INSERT IGNORE INTO %s (accident_id,image_link) VALUES(%d,'%s')", DefineName.ACCIDENT_IMAGE_TABLE_NAME, existedHistoryId.get(), listImage[i]);
                                    stmt = connection.prepareStatement(queryStr);
                                    if (stmt.executeUpdate() <= 0) {
                                        isSuccess = false;
                                        break;
                                    }
                                }
                            }
                            if (isSuccess) {
                                connection.setAutoCommit(true);
                                ret = 0;
                            }

                        }
                    }
                }

            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".mergeInfoWithExistedHistory: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public String getListLinkImageByHistoryId(long historyId) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String ret = "";

        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();

            String queryStr = String.format("SELECT * FROM %s WHERE `accident_id`=%d", DefineName.ACCIDENT_IMAGE_TABLE_NAME, historyId);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();

            if (rs != null) {
                while (rs.next()) {
                    ret += rs.getString("image_link");
                    if (!rs.isLast()) {
                        ret += DefineName.SPLIT_LINK_IMAGE_STRING;
                    }
                }

            }

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".getListLinkImageByHistoryId: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;

    }
}
