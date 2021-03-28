package com.epam.esm.persistence.pool;

/**
 * The Class DbParameter.
 */
public final class DbParameter {

    /** The Constant DB_DRIVER. */
    public static final String DB_DRIVER="db.driver";
    
    /** The Constant DB_URL. */
    public static final String DB_URL="db.url";
    
    /** The Constant DB_USER. */
    public static final String DB_USER="db.user";
    
    /** The Constant DB_PASSWORD. */
    public static final String DB_PASSWORD="db.password";
    
    /** The Constant DB_POOL_SIZE. */
    public static final String DB_POOL_SIZE="db.initpoolsize";

    /** The Constant DB_POOL_SIZE. */
    public static final String DB_MAX_POOL_SIZE="db.maxpoolsize";

    /** The Constant DB_TIMEOUT. */
    public static final String DB_TIMEOUT="db.timeout";

    /** The Constant DB_TIMEOUT. */
    public static final String DB_ATTEMPTIONS="db.attempt";

    /** The Constant DB_ENCODING. */
    public static final String DB_ENCODING="db.encoding";

    /** The Constant DB_TIMEZONE. */
    public static final String DB_TIMEZONE="db.timezone";

    /** The Constant DB_USE_UNICODE. */
    public static final String DB_USE_UNICODE="db.useUnicode";
    public static final String DB_MAX_OPENED_PS = "db.maxOpenedPreparedStatements";


    /**
   * Instantiates a new db parameter.
   */
  private DbParameter(){}
}
