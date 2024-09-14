package com.epam.homework;


import com.epam.homework.config.AppConfig;
import com.epam.homework.model.TableDefinition;
import com.epam.homework.model.CustomType;
import com.epam.homework.service.DatabaseService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      var service = context.getBean(DatabaseService.class);
      var types = List.of(CustomType.INT, CustomType.REAL, CustomType.BOOLEAN, CustomType.TEXT, CustomType.TIMESTAMP);
      var random = new Random();
      var rows = random.nextInt(50);
      var columns = random.nextInt(20);
      var tableDefinitions = IntStream.range(1, 31)
              .mapToObj(i -> new TableDefinition(
                      columns,
                      types,
                      rows == 0 ? 10 : rows));
      service.generateTables(tableDefinitions.toList());
    }
}
