package com.epam.esm.persistence.pool;

import com.epam.esm.persistence.resource.DbResourceManager;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBCPDataSource {

    private static BasicDataSource ds = new BasicDataSource();

    static {
        ds.setUrl(DbResourceManager.getInstance().getValue(DbParameter.DB_URL));
        ds.setUsername(DbResourceManager.getInstance().getValue(DbParameter.DB_USER));
        ds.setPassword(DbResourceManager.getInstance().getValue(DbParameter.DB_PASSWORD));
        ds.setConnectionProperties(
                "serverTimezone="
                        + DbResourceManager.getInstance().getValue(DbParameter.DB_TIMEZONE)
                        + ";useUnicode="
                        + DbResourceManager.getInstance().getValue(DbParameter.DB_USE_UNICODE));
        ds.setDriverClassName(DbResourceManager.getInstance().getValue(DbParameter.DB_DRIVER));

        ds.setMinIdle(Integer.parseInt(
                DbResourceManager.getInstance().getValue(DbParameter.DB_POOL_SIZE)));
        ds.setMaxIdle(Integer.parseInt(
                DbResourceManager.getInstance().getValue(DbParameter.DB_MAX_POOL_SIZE)));
        ds.setMaxOpenPreparedStatements(Integer.parseInt(
                DbResourceManager.getInstance().getValue(DbParameter.DB_MAX_OPENED_PS)));
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DBCPDataSource(){ }

    @Bean
    public static BasicDataSource getDataSource() {
        return ds;
    }
}
