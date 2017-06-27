package com.auge.db;

import com.auge.utils.Props;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Created by lixun on 2017/6/26.
 */
public class DataSourceUtils {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DataSourceUtils.class);

    public static AugeDataSource getDataSource(Props props) {
        String databaseType = props.getString("database.type");

        AugeDataSource dataSource = null;
        if (databaseType.equals("mysql")) {
            int port = props.getInt("mysql.port");
            String host = props.getString("mysql.host");
            String database = props.getString("mysql.database");
            String user = props.getString("mysql.user");
            String password = props.getString("mysql.password");
            int numConnections = props.getInt("mysql.numconnections");
            dataSource = getMySQLDataSource(host, port, database, user, password, numConnections);
        }
        return dataSource;
    }

    public static AugeDataSource getMySQLDataSource(String host, Integer port,
                                                    String dbName, String user, String password, Integer numConnections) {
        return new MySQLBasicDataSource(host, port, dbName, user, password,
                numConnections);
    }

    public static class MySQLBasicDataSource extends AugeDataSource {

        private static MonitorThread monitorThread = null;

        private MySQLBasicDataSource(String host, int port, String dbName,
                                     String user, String password, int numConnections) {
//            super();

            String url = "jdbc:mysql://" + (host + ":" + port + "/" + dbName);
            addConnectionProperty("useUnicode", "yes");
            addConnectionProperty("characterEncoding", "UTF-8");
            setDriverClassName("com.mysql.jdbc.Driver");
            setUsername(user);
            setPassword(password);
            setUrl(url);
            setMaxActive(numConnections);
            setValidationQuery("/* ping */ select 1");
            setTestOnBorrow(true);

//            if (monitorThread == null) {
//                monitorThread = new MonitorThread(this);
//                monitorThread.start();
//            }
        }

        @Override
        public boolean allowsOnDuplicateKey() {
            return true;
        }

        @Override
        public String getDBType() {
            return "mysql";
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }

        private class MonitorThread extends Thread {
            private static final long MONITOR_THREAD_WAIT_INTERVAL_MS = 30 * 1000;
            private boolean shutdown = false;
            MySQLBasicDataSource dataSource;

            public MonitorThread(MySQLBasicDataSource mysqlSource) {
                this.setName("MySQL-DB-Monitor-Thread");
                dataSource = mysqlSource;
            }

            @SuppressWarnings("unused")
            public void shutdown() {
                shutdown = true;
                this.interrupt();
            }

            public void run() {
                while (!shutdown) {
                    synchronized (this) {
                        try {
                            pingDB();
                            wait(MONITOR_THREAD_WAIT_INTERVAL_MS);
                        } catch (InterruptedException e) {
                            logger.info("Interrupted. Probably to shut down.");
                        }
                    }
                }
            }

            private void pingDB() {
                Connection connection = null;
                try {
                    connection = dataSource.getConnection();
                    PreparedStatement query = connection.prepareStatement("SELECT 1");
                    query.execute();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    logger.error("MySQL connection test failed. Please check MySQL connection health!");
                } finally {
                    DbUtils.closeQuietly(connection);
                }
            }
        }
    }
}