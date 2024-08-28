package com.epam.homework2;

import com.epam.homework2.facade.BookingFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookingFacade bookingFacade = context.getBean(BookingFacade.class);
    }
}
