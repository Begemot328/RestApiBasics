package com.epam.esm.persistence.pool;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBCPDataSource extends BasicDataSource {

    private BasicDataSource ds = new BasicDataSource();

    @Value("${database.db.url}")
    private String url;
    @Value("${database.db.user}")
    private String login;
    @Value("${database.db.password}")
    private String password;
    @Value("${database.db.driver}")
    private String driver;
    @Value("${database.db.timezone}")
    private String serverTimezone;
    @Value("${database.db.useUnicode}")
    private String useUnicode;
    @Value("${database.db.initpoolsize}")
    private String initpoolsize;
    @Value("${database.db.maxpoolsize}")
    private String maxpoolsize;
    @Value("${database.db.timezone}")
    private String maxOpenedPreparedStatements;

    {
        ds.setUrl(url);
        ds.setUsername(login);
        ds.setPassword(password);
        ds.setConnectionProperties("serverTimezone=" + serverTimezone
                + ";useUnicode=" + useUnicode);
        ds.setDriverClassName(driver);
        ds.setMinIdle(Integer.parseInt(initpoolsize));
        ds.setMaxIdle(Integer.parseInt(maxpoolsize));
        ds.setMaxOpenPreparedStatements(Integer.parseInt(maxOpenedPreparedStatements));
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DBCPDataSource(){ }
}
