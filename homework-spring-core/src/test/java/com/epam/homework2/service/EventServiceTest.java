package com.epam.homework2.service;

import com.epam.homework2.dao.EventDao;
import com.epam.homework2.dao.InMemoryStorage;
import com.epam.homework2.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EventService")
class EventServiceTest {

    private Map<Long, Event> events = new HashMap<>();

    private InMemoryStorage storage = new InMemoryStorage();

    private EventDao dao;

    private EventService service = new EventService();

    @BeforeEach
    public void beforeEach() {
        var now = Instant.now();
        events.clear();
        Event e1 = new Event(1, "Concert", Date.from(now));
        Event e2 = new Event(2, "Play", Date.from(now));
        Event e3 = new Event(3, "Football Match", Date.from(now));
        Event e4 = new Event(4, "Walk", Date.from(now));
        events.put(e1.id(), e1);
        events.put(e2.id(), e2);
        events.put(e3.id(), e3);
        events.put(e4.id(), e4);
        storage.initialize(Map.of(), events, Map.of());
        dao = new EventDao();
        dao.setEvents(storage);
        service.setDao(dao);
    }

    @Test
    @DisplayName("should get Events by title")
    void getEventsByTitle() {
        var result = service.getEventsByTitle("Concert", 1, 1);
        assertNotNull(result);

        result = service.getEventsByTitle("In-existing event", 1, 1);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should get the Events for the day")
    void getEventsForDay() {
        var today = Date.from(Instant.now());
        var result = service.getEventsForDay(today, 5, 1);
        assertEquals(events.size(), result.size());

        var yesterday = Instant.now().minusSeconds(60 * 60 * 24);
        var res2 = service.getEventsForDay(Date.from(yesterday), 5, 1);
        assertTrue(res2.isEmpty());
    }

    @Test
    @DisplayName("should create an Event")
    void createEvent() {
        var newEvent = new Event(8, "Magic show", Date.from(Instant.now()));
        var created = service.createEvent(newEvent);
        assertNotNull(created);

        var fetched = service.getEventById(newEvent.id());
        assertEquals(fetched, newEvent);
    }

    @Test
    @DisplayName("should update an existing Event")
    void updateEvent() {
        var toUpdate = new Event(1, "Circus", Date.from(Instant.now()));
        var updated = service.updateEvent(toUpdate);
        var fetched = service.getEventById(updated.id());
        assertEquals(toUpdate, updated);
        assertEquals(fetched, updated);

        var inexistent = new Event(100, "Inexistent", Date.from(Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> {
            service.updateEvent(inexistent);
        });
    }

    @Test
    @DisplayName("should delete an Event")
    void deleteEvent() {
        assertTrue(service.deleteEvent(1));
        assertNull(service.getEventById(1L));
        assertFalse(service.deleteEvent(121));
    }
}