package com.epam.esm.services;

import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.service.impl.TagServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServiceRunner {
    public static void main(String[] args) throws DAOSQLException, ServiceException {

        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext("com.epam.esm");
        System.out.println(context.getBean(TagServiceImpl.class).read(1));
    }
}
