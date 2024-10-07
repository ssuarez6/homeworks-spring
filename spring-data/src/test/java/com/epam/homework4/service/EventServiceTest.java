package com.epam.homework4.service;

import com.epam.homework4.dao.EventRepository;
import com.epam.homework4.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("EventService")
class EventServiceTest {

    @Mock private EventRepository repo;

    @InjectMocks private EventService service;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should get Events by title")
    void getEventsByTitle() {
        var mockEvent = new Event("Concert", Date.from(Instant.now()), 10);
        var mockPage = new PageImpl<>(List.of(mockEvent));
        when(repo.findByTitle("Concert", PageRequest.of(0, 10))).thenReturn(mockPage);
        var result = service.getEventsByTitle("Concert", 10, 0);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockEvent.getTitle(), result.get(0).getTitle());

        var mockPage2 = new PageImpl<Event>(Collections.emptyList());
        when(repo.findByTitle("In-existing event", PageRequest.of(0, 10))).thenReturn(mockPage2);
        result = service.getEventsByTitle("In-existing event", 10, 0);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should get the Events for the day")
    void getEventsForDay() {
        var today = Date.from(Instant.now());
        var mockEvent = new Event("Circus", today, 17);
        var mockPage = new PageImpl<>(List.of(mockEvent));
        when(repo.findByDate(today, PageRequest.of(0, 10))).thenReturn(mockPage);
        var result = service.getEventsForDay(today, 10, 0);
        assertNotNull(result);
        assertEquals(result.get(0).getTitle(), mockEvent.getTitle());

        var yesterday = Instant.now().minusSeconds(60 * 60 * 24);
        var mockPage2 = new PageImpl<Event>(Collections.emptyList());
        when(repo.findByDate(Date.from(yesterday), PageRequest.of(0, 10))).thenReturn(mockPage2);
        var res2 = service.getEventsForDay(Date.from(yesterday), 10, 0);
        assertTrue(res2.isEmpty());
    }

    @Test
    @DisplayName("should create an Event")
    void createEvent() {
        var newEvent = new Event("Magic show", Date.from(Instant.now()), 24);
        newEvent.setId(10);
        when(repo.save(newEvent)).thenReturn(newEvent);
        var created = service.createEvent(newEvent);
        assertNotNull(created);

        when(repo.findById(10L)).thenReturn(Optional.of(newEvent));
        var fetched = service.findById(newEvent.getId());
        assertTrue(fetched.isPresent());
        assertEquals(fetched.get(), newEvent);
    }

    @Test
    @DisplayName("should update an existing Event")
    void updateEvent() {
        var toUpdate = new Event("Circus", Date.from(Instant.now()), 25);
        toUpdate.setId(12L);
        when(repo.findById(12L)).thenReturn(Optional.of(toUpdate));
        when(repo.save(toUpdate)).thenReturn(toUpdate);
        var updated = service.updateEvent(toUpdate);
        assertEquals(toUpdate, updated);

        var inexistent = new Event("Inexistent", Date.from(Instant.now()), 22);
        inexistent.setId(13L);
        when(repo.findById(13L)).thenReturn(Optional.empty());
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    service.updateEvent(inexistent);
                });
    }

    @Test
    @DisplayName("should delete an Event")
    void deleteEvent() {
        var mockEvent = new Event("Concert", Date.from(Instant.now()), 22);
        when(repo.findById(18L)).thenReturn(Optional.of(mockEvent));
        assertTrue(service.deleteEvent(18L));
        when(repo.findById(121L)).thenReturn(Optional.empty());
        assertFalse(service.deleteEvent(121));
    }
}
