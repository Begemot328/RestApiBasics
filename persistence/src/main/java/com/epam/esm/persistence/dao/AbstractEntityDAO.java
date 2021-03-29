package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Entity;
import com.epam.esm.persistence.exceptions.DAOException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;


public abstract class AbstractEntityDAO<T extends Entity> implements EntityDAO<T>{
    private static final String INSERT_QUERY =
            "INSERT INTO [SCHEMA].[TABLE]([PARAMETERS]) VALUES ([VALUES]);";
    private static final String UPDATE_QUERY =
            "UPDATE [SCHEMA].[TABLE] SET [PARAMETERS] WHERE ID = ?;";
    private static final String DELETE_QUERY =
            "DELETE FROM [SCHEMA].[TABLE] where ID = ?;";

    private static final String SCHEMA = "[SCHEMA]";
    private static final String TABLE = "[TABLE]";
    private static final String PARAMETERS = "[PARAMETERS]";
    private static final String VALUES = "[VALUES]";
    private static final String DELIMETER = ", ";
    public static final String ID = "id";



    }
