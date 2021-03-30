package com.epam.esm.persistence;

import com.epam.esm.persistence.dao.tag.TagDAO;
import com.epam.esm.persistence.exceptions.DAOException;
import com.epam.esm.persistence.util.Tester;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class PersistenceRunner {
    public static void main(String[] args) throws DAOException {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext("com.epam.esm");
    }
}
