package com.epam.homework2.service;

import com.epam.homework2.dao.EventDao;
import com.epam.homework2.dao.UserDao;
import com.epam.homework2.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private EventDao dao;

    @Autowired
    public void setDao(EventDao dao) {
        this.dao = dao;
    }

    public Event getEventById(Long id) {
        return dao.getEventById(id);
    }

    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return dao.getFilteredEvents(event -> event.title().equals(title), pageSize, pageNum);
    }

    public List<Event> getEventsForDay(Date date, int pageSize, int pageNum) {
        logger.debug("About to get events for date: " + date.toString());
        return dao.getFilteredEvents(event -> event.date().getYear() == date.getYear() &&
                event.date().getMonth() == date.getMonth() &&
                event.date().getDay() == date.getDay(), pageSize, pageNum);
    }

    public Event createEvent(Event event) {
        dao.save(event);
        logger.info("Successfully created new Event with id: " + event.id());
        return event;
    }

    public Event updateEvent(Event event) {
        if(getEventById(event.id()) == null) {
            logger.error("Trying to update a non-existent event with id: " + event.id());
            throw new IllegalArgumentException("Event does not exist");
        }
        dao.save(event);
        return event;
    }

    public boolean deleteEvent(long id) {
        return dao.deleteEvent(id);
    }

}
