package com.epam.esm.web;

import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.service.impl.TagServiceImpl;
import com.epam.esm.web.controller.TagController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


//Run with disabled WebAppInitializer @EnableMvc
public class WebRunner {
    public static void main(String[] args) throws ServiceException {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext("com.epam.esm");
        System.out.println(context.getBean(TagServiceImpl.class).read(1));

        System.out.println(context.getBean(TagController.class).read(1));
    }
}
