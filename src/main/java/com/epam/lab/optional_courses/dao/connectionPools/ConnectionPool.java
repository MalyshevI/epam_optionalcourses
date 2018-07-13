package com.epam.lab.optional_courses.dao.connectionPools;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.*;

/**
 * The class implements Pool pattern for database connection
 *
 * @author Nikolai Tikhonov <akalji@ya.ru> akalji
 */
public class ConnectionPool {
    private static final Logger log = LogManager.getLogger(ConnectionPool.class);

    private static ConnectionPool instance;
    private ArrayList<Connection> freeConnections = new ArrayList<>();
    private String URL;
    private String user;
    private String password;
    private int maxConn;


    /**
     * The method returns singleton or do lazy initialization of it
     *
     * @return connection pool instance
     * @author Nikolai Tikhonov <akalji@ya.ru> akalji
     */
    public static synchronized ConnectionPool getInstance() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("resources/database.properties"));
        } catch (IOException e) {

        }
        String URL = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        if (instance == null) {
            instance = new ConnectionPool(URL, user, password, 10);
        }
        return instance;
    }


    /**
     * The method returns one of free db connections
     *
     * @return connection object
     * @author Nikolai Tikhonov <akalji@ya.ru> akalji
     */
    public synchronized Connection getConnection() {
        Connection con;
        if (!freeConnections.isEmpty()) {
            con = freeConnections.remove(freeConnections.size() - 1);
            try {
                if (con.isClosed()) {
                    con = getConnection();
                }
            } catch (Exception e) {
                con = getConnection();
            }
        } else {
            con = newConnection();
        }
        return con;
    }


    /**
     * The method returns connection object to connection pool
     *
     * @author Nikolai Tikhonov <akalji@ya.ru> akalji
     */
    public synchronized void freeConnection(Connection con) {
        if ((con != null) && (freeConnections.size() <= maxConn)) {
            freeConnections.add(con);
        }
    }


    /**
     * The method closes all connections
     *
     * @author Nikolai Tikhonov <akalji@ya.ru> akalji
     */
    public synchronized void release() {
        for (Connection con : freeConnections) {
            try {
                con.close();
            } catch (SQLException e) {
                log.log(Level.ERROR, "Connection closing error", e);
            }
        }
        freeConnections.clear();
    }


    private ConnectionPool(String URL, String user, String password, int maxConn) {
        this.URL = URL;
        this.user = user;
        this.password = password;
        this.maxConn = maxConn;
    }


    private Connection newConnection() {
        Connection con;
        try {
            if (user == null) {
                con = DriverManager.getConnection(URL);
            } else {
                con = DriverManager.getConnection(URL, user, password);
            }
        } catch (SQLException e) {
            log.log(Level.ERROR, "Connection opening error", e);
            throw new RuntimeException(e);
        }
        return con;
    }
}
