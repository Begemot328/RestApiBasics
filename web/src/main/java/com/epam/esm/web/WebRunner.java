package com.epam.esm.web;

import com.epam.esm.persistence.pool.DbParameter;
import com.epam.esm.persistence.resource.DbResourceManager;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.service.TagService;
import com.epam.esm.web.controller.TagController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


//Run with disabled WebAppInitializer @EnableMvc
public class WebRunner {
    public static void main(String[] args) throws ServiceException {
        System.out.println(DbResourceManager.getInstance().getValue(DbParameter.DB_URL));
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext("com.epam.esm");
        System.out.println(context.getBean(TagService.class).read(1));

        System.out.println(context.getBean(TagController.class).read(1));
    }
}
