package com.epam.lab.optional_courses.dao.connectionPools;

import java.io.Closeable;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class simpleConnectionPool implements Closeable {
    private static final simpleConnectionPool INSTANCE = new simpleConnectionPool();

    private List<PooledConnection> freeConnections;

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private Integer connectionNumber;
    private Semaphore semaphore;

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setConnectionNumber(Integer connectionNumber) {
        this.connectionNumber = connectionNumber;
    }

    private simpleConnectionPool() {}

    public void initConnections() throws SQLException {
        semaphore = new Semaphore(connectionNumber);
        freeConnections = new ArrayList<>(connectionNumber);
        for (int i = 0; i < connectionNumber; i++) {
            freeConnections.add(new PooledConnection(DriverManager.getConnection(url, username, password)));
        }
        log.log(Level.INFO, connectionNumber + " connections created");
    }

    public static ConnectionPool getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws InterruptedException {
        try {
            semaphore.acquire();
            return freeConnections.remove(freeConnections.size() - 1);
        } catch (InterruptedException ex) {
            log.log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    public void close() {
        int releaseNumber = connectionNumber - semaphore.availablePermits();
        semaphore.release(releaseNumber);
    }

    private class PooledConnection implements Connection {

        @Delegate(excludes = AutoCloseable.class)
        private final Connection connection;

        public PooledConnection(Connection c) throws SQLException {
            this.connection = c;
            this.connection.setAutoCommit(true);
        }

        @Override
        public void close() throws SQLException {
            connection.setReadOnly(false);
            freeConnections.add(this);
            semaphore.release();
        }
    }




}
