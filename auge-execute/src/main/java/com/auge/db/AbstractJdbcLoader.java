package com.auge.db;

import java.io.IOException;
import java.sql.Connection;

import com.auge.utils.Props;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

/**
 * Created by lixun on 2017/6/26.
 */
public abstract class AbstractJdbcLoader {
    private AugeDataSource dataSource;

    public AbstractJdbcLoader(Props props) {
        dataSource = DataSourceUtils.getDataSource(props);
    }

    protected Connection getDBConnection(boolean autoCommit) throws IOException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(autoCommit);
        } catch (Exception e) {
            DbUtils.closeQuietly(connection);
            throw new IOException("Error getting DB connection.", e);
        }
        return connection;
    }

    protected QueryRunner createQueryRunner() {
        return new QueryRunner(dataSource);
    }

    protected boolean allowsOnDuplicateKey() {
        return dataSource.allowsOnDuplicateKey();
    }


}
