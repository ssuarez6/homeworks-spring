package com.epam.homework2.dao;

import com.epam.homework2.model.Event;
import com.epam.homework2.model.Ticket;
import com.epam.homework2.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class StorageInitializer implements BeanPostProcessor {

    private Logger logger = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${data.events.file.path}")
    private String eventsFilePath;

    @Value("${data.tickets.file.path}")
    private String ticketsFilePath;

    @Value("${data.users.file.path}")
    private String usersFilePath;

    public StorageInitializer(){
        logger.info("Constructing storageInitializer with no-arg constructor");
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof InMemoryStorage storage) {
            var events = initEventsData();
            var tickets = initTicketsData();
            var users = initUsersData();
            storage.initialize(users, events, tickets);
        }
        return bean;
    }

    private Map<Long, Event> initEventsData(){
        Map<Long, Event> events = new HashMap<>();
        logger.info("Loading events file data with path {}", eventsFilePath);
        try(BufferedReader reader = new BufferedReader(new FileReader(eventsFilePath))){
            String line;
            while((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                long id = Long.parseLong(fields[0]);
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(fields[2]);
                events.put(id, new Event(id, fields[1], date));
            }
        } catch (Exception ex) {
            logger.error("Error while initializing events data", ex);
        }
        return events;
    }

    private Map<Long, User> initUsersData() {
        Map<Long, User> users = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(usersFilePath))){
            String line;
            while((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                long id = Long.parseLong(fields[0]);
                users.put(id, new User(id, fields[1], fields[2]));
            }
        } catch (Exception ex) {
            logger.error("Error while initializing users data", ex);
        }
        return users;
    }

    private Map<Long, Ticket> initTicketsData(){
        Map<Long, Ticket> tickets = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(ticketsFilePath))){
            String line;
            while((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                long id = Long.parseLong(fields[0]);
                long eventId = Long.parseLong(fields[1]);
                long userId = Long.parseLong(fields[2]);
                Ticket.Category category = Ticket.Category.valueOf(fields[3]);
                int place = Integer.parseInt(fields[4]);
                tickets.put(id, new Ticket(id, eventId, userId, category, place));
            }
        } catch (Exception ex) {
            logger.error("Error while initializing tickets data", ex);
        }
        return tickets;
    }
}
