package com.epam.esm.persistence;

import com.epam.esm.persistence.dao.TagDAO;
import com.epam.esm.persistence.exceptions.DAOException;
import com.epam.esm.persistence.pool.DbParameter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class PersistenceRunner {
    public static void main(String[] args) throws DAOException {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext("com.epam.esm");
        System.out.println(context.getBean(TagDAO.class).read(1));
    }
}
