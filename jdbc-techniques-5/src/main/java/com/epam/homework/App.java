package com.epam.homework;

import com.epam.homework.config.AppConfig;
import com.epam.homework.dao.CustomerDAO;
import com.epam.homework.dao.DatabaseDAO;
import com.epam.homework.dao.OrderDAO;
import com.epam.homework.dao.ProductDAO;
import com.epam.homework.model.Customer;
import com.epam.homework.model.Product;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        var databaseDAO = context.getBean(DatabaseDAO.class);
        databaseDAO.createTables();
        databaseDAO.createStoredProcedure();

        var customerDAO = context.getBean(CustomerDAO.class);
        IntStream.range(1, 11).forEach(i -> {
            var c = Customer.random();
            customerDAO.save(c);
        }); //10 customers from 1 to 10

        var productDAO = context.getBean(ProductDAO.class);
        IntStream.range(1, 41).forEach(i -> {
            var p = Product.random();
            productDAO.save(p);
        }); // 40 products with ids from 1 to 40

        var orderDAO = context.getBean(OrderDAO.class);
        var result = orderDAO.processOrder(1, new Integer[]{1, 4, 8, 10, 21}, new Integer[] {3, 5, 1, 1, 2});

        System.out.printf("Result from processing order: %d", result);
        //var result1 = orderDAO.processOrder(2, new Integer[]{15, 21, 35, 10, 23, 7}, new Integer[] {1, 3, 5, 2, 5, 6});
        //var result2 = orderDAO.processOrder(3, new Integer[]{1, 4, 8, 10, 21}, new Integer[] {3, 5, 1, 1, 2});
    }
}
