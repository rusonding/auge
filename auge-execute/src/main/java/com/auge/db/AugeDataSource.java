package com.auge.db;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * Created by lixun on 2017/6/26.
 */
public abstract class AugeDataSource extends BasicDataSource {
    public abstract boolean allowsOnDuplicateKey();
    public abstract String getDBType();
}
