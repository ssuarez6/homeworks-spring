package com.epam.homework2.dao;

import com.epam.homework2.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;

@Repository
public class EventDao {
    private InMemoryStorage events;

    @Autowired
    public void setEvents(InMemoryStorage storage) {
        this.events = storage;
    }

    public Event getEventById(Long id) {
        return events.getEventsMap().get(id);
    }

    public void save(Event event) {
        events.getEventsMap().put(event.id(), event);
    }

    public boolean deleteEvent(Long id) {
        if(events.getEventsMap().containsKey(id)){
            events.getEventsMap().remove(id);
            return true;
        } else return false;
    }

    public List<Event> getFilteredEvents(Predicate<Event> predicate, int pageSize, int pageNum) {
        var filteredEvents = events.getEventsMap().values().stream().filter(predicate).toList();

        int fromIndex = (pageNum -1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filteredEvents.size());

        return filteredEvents.subList(fromIndex, toIndex);
    }
}
