package com.quantity.measurement.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import com.quantity.measurement.config.ApplicationConfig;

/**
 * Simple custom JDBC connection pool.
 */
public class ConnectionPool {

    private static final Queue<Connection> availableConnections = new LinkedList<>();

    private static final int INITIAL_SIZE = ApplicationConfig.getIntProperty("pool.initialSize");

    private static final int MAX_SIZE = ApplicationConfig.getIntProperty("pool.maxSize");

    private static final long TIMEOUT = ApplicationConfig.getIntProperty("pool.timeout");

    private static int currentSize = 0;

    /**
     * Initialize pool with predefined connections.
     */
    static {

        try {

            for (int i = 0; i < INITIAL_SIZE; i++) {

                availableConnections.offer(createConnection());

                currentSize++;
            }

        } catch (SQLException e) {

            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    /**
     * Create new database connection.
     */
    private static Connection createConnection() throws SQLException {

        return DriverManager.getConnection(

                ApplicationConfig.getProperty("spring.datasource.url"),

                ApplicationConfig.getProperty("spring.datasource.username"),

                ApplicationConfig.getProperty("spring.datasource.password")
        );
    }

    /**
     * Acquire connection from pool.
     */
    public static synchronized Connection getConnection() throws SQLException {

        long startTime = System.currentTimeMillis();

        while (availableConnections.isEmpty()) {

            if (currentSize < MAX_SIZE) {

                currentSize++;

                return createConnection();
            }

            if (System.currentTimeMillis() - startTime > TIMEOUT) {

                throw new SQLException("Connection timeout");
            }

            try {

                Thread.sleep(100);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                throw new SQLException( "Thread interrupted", e);
            }
        }

        return availableConnections.poll();
    }

    /**
     * Release connection back to pool.
     */
    public static synchronized void releaseConnection(Connection connection) {

        try {

            if (connection != null && !connection.isClosed()) {

                availableConnections.offer(connection);
            }

        } catch (SQLException e) {

            throw new RuntimeException("Failed to release connection", e);
        }
    }

    /**
     * Get available connection count.
     */
    public static synchronized int availableConnections() {

        return availableConnections.size();
    }

    /**
     * Get total connection count.
     */
    public static synchronized int totalConnections() {

        return currentSize;
    }
}