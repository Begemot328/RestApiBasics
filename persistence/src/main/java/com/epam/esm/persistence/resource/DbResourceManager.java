package com.epam.esm.persistence.resource;

import java.util.ResourceBundle;

/**
 * The Class DbResourceManager.
 */
public class DbResourceManager {
    
    /** The Constant DATABASE. */
    private static final String DATABASE="database";
    
    /** The Constant instance. */
    private final static DbResourceManager instance=new DbResourceManager();
    
    /** The bundle. */
    private final ResourceBundle bundle=ResourceBundle.getBundle(DATABASE);
    
    /**
     * Gets the single instance of DbResourceManager.
     *
     * @return single instance of DbResourceManager
     */
    public static DbResourceManager getInstance(){

        return instance;
    }
    
    /**
     * Gets the value.
     *
     * @param key the key
     * @return the value
     */
    public String getValue(String key){

        return bundle.getString(key);
    }
}
