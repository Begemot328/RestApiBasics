package com.epam.esm.persistence.pool;


import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Connection pool, @{link BasicDataSource} wrapper
 *
 * @author Yury Zmushko
 * @version 1.0
 */

@Configuration
@PropertySource("classpath:database.properties")
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

    /**
     * Default constructor
     */
    public DBCPDataSource(){
        logger.debug("DBCPDataSource() constructor");
    }

    /**
     * Get {@link BasicDataSource} method
     *
     * @return {@link BasicDataSource}  object with set connection parameters
     */
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

    @Bean
    public static JdbcTemplate getTemplate(@Qualifier("getDataSource") BasicDataSource source) {
        return new JdbcTemplate(source);
    }
}
