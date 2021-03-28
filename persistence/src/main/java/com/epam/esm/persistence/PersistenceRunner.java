package com.epam.esm.persistence;

import com.epam.esm.persistence.dao.tag.TagDAO;
import com.epam.esm.persistence.exceptions.DAOException;
import com.epam.esm.persistence.pool.DbParameter;
import com.epam.esm.persistence.resource.DbResourceManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class PersistenceRunner {
    public static void main(String[] args) throws DAOException {
        System.out.println(DbResourceManager.getInstance().getValue(DbParameter.DB_URL));
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext("com.epam.esm");
        System.out.println(context.getBean(TagDAO.class).read(1));
    }
}
