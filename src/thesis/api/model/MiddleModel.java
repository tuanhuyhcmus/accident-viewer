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
import java.util.concurrent.atomic.AtomicLong;
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
public class MiddleModel {

    private static MiddleModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());

    public static MiddleModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new MiddleModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public int checkIsActiveAccident(History history,AtomicLong exisedHistoryId,int type) {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = 0;

        try {

            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;

            String sessionTableName = DefineName.SESSION_TABLE_NAME;

            queryStr = String.format("SELECT * FROM %s WHERE `history_id` IN (SELECT `history_id` FROM %s WHERE `type`=%d)", sessionTableName,DefineName.HISTORY_TABLE_NAME,type);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            double lat1=history.getLat();
            double lng1=history.getLng();
            double lat2=0;
            double lng2=0;
            if (rs != null) {
                while (rs.next()) {
                   lat2=rs.getDouble("lat");
                   lng2=rs.getDouble("lng");
                   if(distanceBetweenTwoLocation(lat1, lat2, lng1, lng2, 0, 0)<=AppConst.EPSILON_DISTANCE)
                   {
                       exisedHistoryId.set(rs.getLong("history_id"));
                       ret=1;
                       break;
                   }
                    
                } 
            }

        } catch (Exception ex) {
            ret=-1;
            logger.error(getClass().getSimpleName() + ".checkIsTrustyUser: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public static double distanceBetweenTwoLocation(double lat1, double lat2, double lon1,
            double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

}
