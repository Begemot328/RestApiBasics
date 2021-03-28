package com.epam.esm.services;

import com.epam.esm.persistence.exceptions.DAOException;
import com.epam.esm.persistence.pool.DbParameter;
import com.epam.esm.persistence.resource.DbResourceManager;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.service.TagService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServiceRunner {
    public static void main(String[] args) throws DAOException, ServiceException {

        System.out.println(DbResourceManager.getInstance().getValue(DbParameter.DB_URL));
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext("com.epam.esm");
        System.out.println(context.getBean(TagService.class).read(1));
    }
}
