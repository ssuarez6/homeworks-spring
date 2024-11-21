package com.epam.homework.service;

import com.epam.homework.repository.EventRepository;
import com.epam.homework.repository.UserRepository;
import com.epam.homework.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private EventRepository repo;

    @Autowired
    public void setRepo(EventRepository repo) {
        this.repo = repo;
    }

    public Optional<Event> findById(Long id) {
        return repo.findById(id);
    }

    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return repo.findByTitle(title, PageRequest.of(pageNum, pageSize)).stream().toList();
    }

    public List<Event> getEventsForDay(Date date, int pageSize, int pageNum) {
        logger.info("About to get events for date: " + date.toString());
        var result = repo.findByDate(date, PageRequest.of(pageNum, pageSize)).stream().toList();
        logger.debug("Found {} events by date.", result.size());
        return result;
    }

    public Event createEvent(Event event) {
        var created = repo.save(event);
        logger.info("Successfully created new Event with id {} ", created.getId());
        return event;
    }

    public Event updateEvent(Event event) {
        var optionalEvent = repo.findById(event.getId());
        optionalEvent.ifPresentOrElse(
                existing -> {
                    repo.save(event);
                },
                () -> {
                    logger.error("Trying to update a non-existent event with id: " + event.getId());
                    throw new IllegalArgumentException("Event does not exist");
                });

        return event;
    }

    public boolean deleteEvent(long id) {
        var found = repo.findById(id);
        if (found.isEmpty()) return false;
        repo.deleteById(id);
        return true;
    }

    public List<Event> getAll() {
        return repo.findAll();
    }
}
