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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import thesis.api.common.AppConst;
import thesis.api.common.DefineName;
import thesis.api.data.Schedule;
import thesis.api.data.User;
import thesis.api.database.MySqlFactory;

/**
 *
 * @author huynct
 */
public class UserModel {

    private static UserModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());

    public static UserModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new UserModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public int insertUser(User user) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int ret = -1;

        try {

            connection = MySqlFactory.getConnection();
            String queryStr;
            String userTableName = DefineName.USER_TABLE_NAME;

            queryStr = String.format("INSERT INTO %s (`user_name`,`password`,`phone`,`email`,`name`,`trust_point`,`address`) VALUES('%s','%s','%s','%s','%s',%d,'%s')", userTableName, user.getUserName(), user.getPassword(), user.getPhone(), user.getEmail(),user.getName(), AppConst.DEFAULT_INIT_TRUSTY_POINT, user.getAddress());
            stmt = connection.prepareStatement(queryStr, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs != null) {
                if (rs.next()) {
                    user.setUserId(rs.getLong(1));
                    ret = 0;
                }
            }

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".insertUser: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int checkIsTrustyUser(long userId) {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;

        try {

            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;

            String userTableName = DefineName.USER_TABLE_NAME;

            queryStr = String.format("SELECT * FROM %s WHERE `user_id` = %d AND `trust_point` >= %d", userTableName, userId, AppConst.TRUST_POINT_ACCEPTABLE);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();

            if (rs != null) {
                if (rs.next()) {
                    ret = 0;
                } else {
                    ret = 1;
                }
            }

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".checkIsTrustyUser: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int getUserByUserName(User user) {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;

        try {

            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;

            String userTableName = DefineName.USER_TABLE_NAME;

            queryStr = String.format("SELECT * FROM %s WHERE `user_name` = '%s' AND `marked_delete`=%d ", userTableName, user.getUserName(), AppConst.MARKED_NONE_DELETE);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();

            if (rs != null) {
                if (rs.next()) {
                    user.setAddress(rs.getString("address"));
                    user.setEmail(rs.getString("email"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setTrust_point(rs.getInt("trust_point"));
                    user.setUserId(rs.getInt("user_id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setPhone(rs.getString("phone"));
                    ret = 0;
                } else {
                    ret = 1;
                }
            }

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".getUserByUserName: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int updateUser(User user) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;
            String userTableName = DefineName.USER_TABLE_NAME;
            queryStr = String.format("UPDATE %s SET `user_name`='%s',`password`='%s',`phone`='%s',`email`='%s',`name`='%s',`trust_point`=%d,`address`='%s' WHERE `user_id`=%d", userTableName, user.getUserName(), user.getPassword(), user.getPhone(), user.getEmail(), user.getName(),AppConst.DEFAULT_INIT_TRUSTY_POINT, user.getAddress(),user.getUserId());
            if (stmt.executeUpdate(queryStr) > 0) {
                ret = 0;
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateUser: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;

    }

}
