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
public class SessionModel {

    private static SessionModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());

    public static SessionModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new SessionModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public int insertSession(History history) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;

        try {

            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;

            String sessionTableName = DefineName.SESSION_TABLE_NAME;

            queryStr = String.format("INSERT INTO %s(`lat`,`lng`,`first_post_time`,`end_time` ,`recent_time`,`history_id`) VALUES(%f,%f,'%s','%s','%s',%d)", sessionTableName, history.getLat(), history.getLng(), CommonFunction.getCurrentDateTime(), CommonFunction.getCurrentDateTime(CommonFunction.getCurrentTimeMillis() + AppConst.TIME_TO_END_ACCDIENT), CommonFunction.getCurrentDateTime(), history.getHistoryId());
            if (stmt.executeUpdate(queryStr) > 0) {
                ret = 0;
            }

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".insertSession: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int updateSession(History existedHistory) {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;

        try {

            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;

            String sessionTableName = DefineName.SESSION_TABLE_NAME;

            queryStr = String.format("UPDATE %s SET `recent_time`='%s',`end_time`='%s' WHERE `history_id`=%d", sessionTableName, CommonFunction.getCurrentDateTime(), CommonFunction.getCurrentDateTime(CommonFunction.getCurrentTimeMillis() + AppConst.TIME_TO_END_ACCDIENT),existedHistory.getHistoryId());
            if (stmt.executeUpdate(queryStr) > 0) {
                ret = 0;
            }

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateSession: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }
}
