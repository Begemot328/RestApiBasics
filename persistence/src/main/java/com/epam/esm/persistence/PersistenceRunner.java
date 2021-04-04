package com.epam.esm.persistence;

import com.epam.esm.persistence.exceptions.DAOSQLException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class PersistenceRunner {
    public static void main(String[] args) throws DAOSQLException {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext("com.epam.esm");
    }
}
