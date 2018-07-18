package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.dao.connectionPools.ConnectionPool;
import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDaoImpl implements UserDao {

    private static final Logger log = LogManager.getLogger(ConnectionPool.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String FIND_ALL;
    private static final String FIND_BY_ID;
//    private static final String FIND_BY_NAME;
    private static final String GET_USER_BY_EMAIL_AND_PASSWORD;
    private static final String INSERT;
    private static final String UPDATE;
    private static final String DELETE;
    private static final String COUNT;

    static {
        Properties properties = new Properties();
        try {
            properties.load(ConnectionPool.class.getClassLoader().getResourceAsStream("sql_request_body_Mysql.properties"));
            log.log(Level.INFO, "SQL request bodies for users loaded successfully");
        } catch (IOException e) {
            log.log(Level.ERROR, "Can't load SQL request bodies for users", e);
        }
        FIND_ALL = properties.getProperty("FIND_ALL_USERS");
        FIND_BY_ID = properties.getProperty("GET_USER_BY_ID");
//        FIND_BY_NAME = properties.getProperty("GET_USER_BY_NAME");
        GET_USER_BY_EMAIL_AND_PASSWORD = properties.getProperty("GET_USER_BY_EMAIL_AND_PASSWORD");
        INSERT = properties.getProperty("ADD_USER");
        UPDATE = properties.getProperty("UPDATE_USER");
        DELETE = properties.getProperty("DELETE_USER");
        COUNT = properties.getProperty("COUNT_USERS");
    }


    @Override
    public List<User> getAllUsers(long offset, long limit) {
        Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> list = new ArrayList<>();
        try {
            stmt = conn.prepareStatement(FIND_ALL);
            stmt.setLong(1, offset);
            stmt.setLong(2, limit);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                String password = rs.getString("password");
                user.setPassword(password);
                user.setEmail(rs.getString("email"));
                user.setAdmin(rs.getBoolean("is_admin"));

                list.add(user);
            }
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(stmt, rs, conn);
        }
        return list;
    }


        @Override
    public long countAllUsers() {
        Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        long result = 0;
        try {
            stmt = conn.prepareStatement(COUNT);
            rs = stmt.executeQuery();
            while (rs.next()){
                result = rs.getLong(1);
            }
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(stmt, rs, conn);
        }
        return result;
    }

    @Override
    public User getUserById(int id) {
        Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(FIND_BY_ID);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            User user;
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                String password = rs.getString("password");
                user.setPassword(password);
                user.setEmail(rs.getString("email"));
                user.setAdmin(rs.getBoolean("is_admin"));

                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(stmt, rs, conn);
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setObject(4, user.getPassword());
            stmt.setBoolean(5, user.isAdmin());

            int result = stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                user.setId(rs.getInt(1));
            }

            return (result > 0);
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(stmt, rs, conn);
        }

        return false;
    }

    @Override
    public boolean updateUser(User user) {
        Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(UPDATE);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setBoolean(5, user.isAdmin());
            stmt.setInt(6, user.getId());

            int result = stmt.executeUpdate();

            return (result > 0);
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(stmt, rs, conn);
        }

        return false;
    }

    @Override
    public boolean deleteUser(User user) {
        Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(DELETE);
            stmt.setInt(1, user.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(stmt, rs, conn);
        }
        return false;
    }

    @Override
    public boolean checkForEmailAndPassword(String email, String password) {
        Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(GET_USER_BY_EMAIL_AND_PASSWORD);
            stmt.setString(1, email);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()
                    && rs.getString("email").equals(email)
                    && rs.getString("password").equals(password)) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(stmt, rs, conn);
        }
        return false;
    }

    public User getByEmailAndPassword(String email, String password) {
        Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(GET_USER_BY_EMAIL_AND_PASSWORD);
            stmt.setString(1,email);
            stmt.setString(2,password);
            rs = stmt.executeQuery();
            User user;
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPassword(password);
                user.setEmail(rs.getString("email"));
                user.setAdmin(rs.getBoolean("is_admin"));

                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.log(Level.ERROR, e);
        } finally {
            closeResources(stmt, rs, conn);
        }
        return null;
    }

    private void closeResources(PreparedStatement statement, ResultSet resultSet, Connection connection) {
        try {
            if (statement != null) statement.close();
            if (resultSet != null) resultSet.close();
            if (connection != null) connection.close();
        } catch (SQLException e1) {
            log.log(Level.ERROR, e1);
            e1.printStackTrace();
        }
    }
}







