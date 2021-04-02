package com.epam.esm.persistence.pool;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBCPDataSource {
    static Logger logger = LoggerFactory.getLogger(DBCPDataSource.class);


    private BasicDataSource ds = new BasicDataSource();

    @Value("${db.url}")
    private String url;
    @Value("${db.user}")
    private String login;
    @Value("${db.password}")
    private String password;
    @Value("${db.driver}")
    private String driver;
    @Value("${db.timezone}")
    private String serverTimezone;
    @Value("${db.useUnicode}")
    private String useUnicode;
    @Value("${db.initpoolsize}")
    private String initpoolsize;
    @Value("${db.maxpoolsize}")
    private String maxpoolsize;
    @Value("${db.maxOpenedPreparedStatements}")
    private String maxOpenedPreparedStatements;

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DBCPDataSource(){
        logger.debug("DBCPDataSource() constructor");
    }

    @Bean
    public BasicDataSource getDataSource() {
        ds.setUrl(url);
        ds.setUsername(login);
        ds.setPassword(password);
        ds.setConnectionProperties("serverTimezone=" + serverTimezone
                + ";useUnicode=" + useUnicode);
        ds.setDriverClassName(driver);
        ds.setMinIdle(Integer.parseInt(initpoolsize));
        ds.setMaxIdle(Integer.parseInt(maxpoolsize));
        ds.setMaxOpenPreparedStatements(Integer.parseInt(maxOpenedPreparedStatements));
        return ds;
    }
}
