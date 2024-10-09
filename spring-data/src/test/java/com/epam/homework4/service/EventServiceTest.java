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
        // Arrange
        var mockEvent = new Event("Concert", Date.from(Instant.now()), 10);
        var mockPage = new PageImpl<>(List.of(mockEvent));
        when(repo.findByTitle("Concert", PageRequest.of(0, 10))).thenReturn(mockPage);

        // Act
        var result = service.getEventsByTitle("Concert", 10, 0);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockEvent.getTitle(), result.get(0).getTitle());

    }

    @Test
    @DisplayName("should return empty list for events that do not exist")
    public void getEventsByTitle_emptyResult() {
        // Arrange
        var mockPage = new PageImpl<Event>(Collections.emptyList());
        when(repo.findByTitle("In-existing event", PageRequest.of(0, 10))).thenReturn(mockPage);

        // Act
        var result = service.getEventsByTitle("In-existing event", 10, 0);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should get the Events for the day")
    void getEventsForDay() {
        // Arrange
        var today = Date.from(Instant.now());
        var mockEvent = new Event("Circus", today, 17);
        var mockPage = new PageImpl<>(List.of(mockEvent));
        when(repo.findByDate(today, PageRequest.of(0, 10))).thenReturn(mockPage);

        // Act
        var result = service.getEventsForDay(today, 10, 0);

        // Assert
        assertNotNull(result);
        assertEquals(result.get(0).getTitle(), mockEvent.getTitle());
    }

    @Test
    @DisplayName("should create an Event")
    void createEvent() {
        // Arrange
        var newEvent = new Event("Magic show", Date.from(Instant.now()), 24);
        newEvent.setId(10);

        // Act
        when(repo.save(newEvent)).thenReturn(newEvent);
        var created = service.createEvent(newEvent);

        // Assert
        assertNotNull(created);
    }

    @Test
    @DisplayName("should update an existing Event")
    void updateEvent() {
        // Arrange
        var toUpdate = new Event("Circus", Date.from(Instant.now()), 25);
        toUpdate.setId(12L);
        when(repo.findById(12L)).thenReturn(Optional.of(toUpdate));
        when(repo.save(toUpdate)).thenReturn(toUpdate);

        // Act
        var updated = service.updateEvent(toUpdate);

        // Assert
        assertEquals(toUpdate, updated);

    }

    @Test
    @DisplayName("should not update a non-existent event")
    public void updateEvent_FailWhenNonExistent() {
        // Arrange
        var nonExistant = new Event("NonExistant", Date.from(Instant.now()), 22);
        nonExistant.setId(13L);
        when(repo.findById(13L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    service.updateEvent(nonExistant);
                });
    }

    @Test
    @DisplayName("should delete an Event")
    void deleteEvent() {
        // Arrange
        var mockEvent = new Event("Concert", Date.from(Instant.now()), 22);
        when(repo.findById(18L)).thenReturn(Optional.of(mockEvent));

        // Act
        var result = service.deleteEvent(18L);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("should return false when deleting a non existent event")
    void deleteEvent_ReturnFalse() {
        // Arrange
        when(repo.findById(121L)).thenReturn(Optional.empty());

        // Act
        var result = service.deleteEvent(121);

        // Assert
        assertFalse(result);

    }
}
