package com.epam.lab.optional_courses.dao;

import com.epam.lab.optional_courses.dao.connectionPools.ConnectionPool;
import com.epam.lab.optional_courses.entity.Course;
import com.epam.lab.optional_courses.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoH2Impl implements UserDAO {

    private final Logger log = LogManager.getLogger(ConnectionPool.class);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String FIND_ALL = "SELECT id, first_name, last_name, email,password, is_admin, group_id FROM mydb.users ORDER BY id";
    private static final String FIND_BY_ID = "SELECT id, first_name, last_name, email,password, is_admin, group_id FROM mydb.users WHERE id=?";
    private static final String FIND_BY_NAME = "SELECT id, first_name, last_name, email,password, is_admin, group_id FROM mydb.users WHERE name=?";
    private static final String FIND_BY_EMAIL_AND_PASSWORD = "SELECT FROM mydb.users WHERE email=?, password = ?";
    private static final String INSERT = "INSERT INTO mydb.users(first_name," +
            " last_name," +
            " email," +
            " password," +
            " is_admin," +
            " group_id) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE mydb.user SET first_name = ?," +
            " last_name = ?," +
            " email = ?," +
            " password = ?," +
            " is_admin = ?," +
            " group_id = ?" +
            " WHERE id=?";
    private static final String DELETE = "DELETE FROM mydb.users WHERE id=?";

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

    @Override
    public List<User> getAllUsers() {
        Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> list = new ArrayList<User>();
        try {
            stmt = conn.prepareStatement(FIND_ALL);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                Array a = rs.getArray("password");
                char[] password = (char[]) a.getArray();
                user.setPassword(password);
                user.setEmail(rs.getString("email"));
                user.setAdmin(rs.getBoolean("is_admin"));

                list.add(user);
            }
        }
        catch (SQLException e){
            log.log(Level.ERROR, e);
        }
        finally {
            closeResources(stmt,rs, conn);
        }
        return list;
    }

    @Override
    public User getUserById(int id){
        Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = null;
        User user = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(FIND_BY_ID);
            rs = stmt.executeQuery();

            if (rs.next()){
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                Array a = rs.getArray("password");
                char[] password = (char[]) a.getArray();
                user.setPassword(password);
                user.setEmail(rs.getString("email"));
                user.setAdmin(rs.getBoolean("is_admin"));

                return user;
            }
            else{
                return null;
            }
        }
        catch (SQLException e){
            log.log(Level.ERROR, e);
        }
        finally {
            closeResources(stmt,rs, conn);
        }
        return null;
    }

    @Override
    public boolean addUser(User user){
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

            if (rs.next()){
                user.setId(rs.getInt(1));
            }

            return (result > 0);
        }
        catch (SQLException e){
            log.log(Level.ERROR, e);
        }
        finally {
            closeResources(stmt,rs, conn);
        }

        return false;
    }

    @Override
    public boolean updateUser(User user){
        Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(UPDATE, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setObject(4, user.getPassword());
            stmt.setBoolean(5, user.isAdmin());

            int result = stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();

            if (rs.next()){
                user.setId(rs.getInt(1));
            }

            return (result > 0);
        }
        catch (SQLException e){
            log.log(Level.ERROR, e);
        }
        finally {
            closeResources(stmt,rs, conn);
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
        }
        catch (SQLException e) {
            log.log(Level.ERROR, e);
        }
        finally {
            closeResources(stmt,rs, conn);
        }
        return false;
    }}
