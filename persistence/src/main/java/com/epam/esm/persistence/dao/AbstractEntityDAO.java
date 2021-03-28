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
    protected static final String ID = "id";

    private JdbcTemplate template;

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public JdbcTemplate getTemplate() {
        return this.template;
    }

    /**
     * Create method
     *
     * @param t entity to create
     *
     * @throws DAOException
     */
    public abstract T create(T t) throws DAOException;

    /**
     * Read method
     *
     * @param id of the entity
     *
     * @throws DAOException
     */
    public abstract T read(int id) throws DAOException;

    /**
     * Update method
     *
     * @param t entity to create
     *
     * @throws DAOException
     */
    public abstract void update(T t) throws DAOException;

    /**
     * Delete method
     *
     * @param id of the entity to delete
     *
     * @throws DAOException
     */
    public abstract void delete(int id) throws DAOException;

    /**
     * Find all method
     *
     * @throws DAOException
     */
    public abstract Collection<T> findAll() throws DAOException;
    }
