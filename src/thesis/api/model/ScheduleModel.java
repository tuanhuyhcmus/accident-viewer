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
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import thesis.api.common.DefineName;
import thesis.api.data.Schedule;
import thesis.api.database.MySqlFactory;

/**
 *
 * @author huynct
 */
public class ScheduleModel {

    private static ScheduleModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());

    public static ScheduleModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new ScheduleModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public int insertSchedule(Schedule schedule) {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int ret = -1;

        try {

            connection = MySqlFactory.getConnection();
            String queryStr;

            String scheduleTableName = DefineName.SCHEDULE_TABLE_NAME;

            queryStr = String.format("INSERT INTO %s (`user_id`,`location_name`,`lat`,`lng`,`from_time`,`to_time`) VALUES(%d,'%s',%f,%f,'%s','%s') ", scheduleTableName, schedule.getUserId(), schedule.getLocationName(), schedule.getLat(), schedule.getLng(), schedule.getFromTime(), schedule.getToTime());
            stmt = connection.prepareStatement(queryStr, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                schedule.setScheduleId(rs.getLong(1));
                ret = 0;
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".insertSchedule: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int getListSchedule(String uid, List<Schedule> listSchedule) {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;

        try {

            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;

            String scheduleTableName = DefineName.SCHEDULE_TABLE_NAME;

            queryStr = String.format("SELECT * FROM %s WHERE `user_id` = %d ", scheduleTableName, Long.parseLong(uid));
            stmt.execute(queryStr);
            rs = stmt.getResultSet();

            if (rs != null) {
                while (rs.next()) {
                    Schedule schedule = new Schedule();
                    schedule.setUserId(rs.getLong("user_id"));
                    schedule.setLat(rs.getDouble("lat"));
                    schedule.setLng(rs.getDouble("lng"));
                    schedule.setLocationName(rs.getString("location_name"));
                    schedule.setFromTime(rs.getString("from_time"));
                    schedule.setToTime(rs.getString("to_time"));
                    listSchedule.add(schedule);
                }
            }
            ret = 0;
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".getListSchedule: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int updateSchedule(Schedule schedule) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        try {
            connection = MySqlFactory.getConnection();
            String queryStr;

            String scheduleTableName = DefineName.SCHEDULE_TABLE_NAME;

            queryStr = String.format("UPDATE %s SET `location_name`='%s',`lat`=%f,`lng`=%f,`from_time`='%s',`to_time`='%s' WHERE `user_id`=%d AND `schedule_id`=%d", scheduleTableName, schedule.getLocationName(), schedule.getLat(), schedule.getLng(), schedule.getFromTime(), schedule.getToTime(), schedule.getUserId(),schedule.getScheduleId());
            stmt = connection.prepareStatement(queryStr);
            if (stmt.executeUpdate() > 0) {
                ret = 0;
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".insertSchedule: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }
}
