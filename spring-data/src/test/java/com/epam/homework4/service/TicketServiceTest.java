package com.epam.homework4.service;

import com.epam.homework4.dao.TicketRepository;
import com.epam.homework4.model.Event;
import com.epam.homework4.model.Ticket;
import com.epam.homework4.model.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Captor ArgumentCaptor<UserAccount> userAccountArgumentCaptor;
    @Mock private TicketRepository repo;
    @Mock private UserAccountService userAccountService;
    @Mock private EventService eventService;
    @InjectMocks private TicketService service;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should book a ticket")
    void bookTicket() {
        // Arrange
        var mockTicket = new Ticket(4L, 1L, Ticket.Category.PREMIUM, 1);
        var mockUserAccount = new UserAccount(4, 100);
        var mockUpdatedUserAccount = new UserAccount(4, 50);
        var mockEvent = new Event("Circus", Date.from(Instant.now()), 50);
        when(repo.findBookedTicket(1L, 4L, 1, Ticket.Category.PREMIUM)).thenReturn(Optional.empty());
        when(repo.save(any())).thenReturn(mockTicket);
        when(eventService.findById(4L)).thenReturn(Optional.of(mockEvent));
        when(userAccountService.findByUserId(1L)).thenReturn(Optional.of(mockUserAccount));
        when(userAccountService.update(mockUpdatedUserAccount)).thenReturn(mockUpdatedUserAccount);

        // Act
        var result = service.bookTicket(1L, 4L, 1, Ticket.Category.PREMIUM);

        // Assert
        assertNotNull(result);
        assertEquals(mockTicket, result);
        verify(eventService, times(1)).findById(4L);
        verify(userAccountService, times(1)).findByUserId(1L);
        verify(userAccountService, times(1)).update(userAccountArgumentCaptor.capture());
        var userAccountUsed = userAccountArgumentCaptor.getValue();
        assertEquals(mockUpdatedUserAccount.getMoney(), userAccountUsed.getMoney());
    }

    @Test
    @DisplayName("should not book a ticket if user does not have enough money")
    void bookTicket_failWithInsufficientFunds() {
        // Arrange
        var mockUserAccount = new UserAccount(4, 20);
        var mockEvent = new Event("Circus", Date.from(Instant.now()), 50);
        when(repo.findBookedTicket(1L, 4L, 1, Ticket.Category.PREMIUM)).thenReturn(Optional.empty());
        when(eventService.findById(4L)).thenReturn(Optional.of(mockEvent));
        when(userAccountService.findByUserId(1L)).thenReturn(Optional.of(mockUserAccount));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.bookTicket(1L, 4L, 1, Ticket.Category.PREMIUM);
        });
        verify(eventService, times(1)).findById(4L);
        verify(userAccountService, times(1)).findByUserId(1L);
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("should fail booking an already booked ticket")
    void notBookTicket() {
        // Arrange
        var mockTicket = new Ticket(1L, 1L, Ticket.Category.BAR, 1);
        when(repo.findBookedTicket(1L, 1L, 1, Ticket.Category.BAR)).thenReturn(Optional.of(mockTicket));

        // Act & Assert
        assertThrows(
                IllegalStateException.class,
                () -> {
                    service.bookTicket(1L, 1L, 1, Ticket.Category.BAR);
                });
    }

    @Test
    @DisplayName("should get all tickets for a user")
    void getBookedTicketsForUser() {
        // Arrange
        var mockTicket = new Ticket(1L, 1L, Ticket.Category.BAR, 1);
        var mockTicket2 = new Ticket(1L, 1L, Ticket.Category.BAR, 2);
        var mockPage = new PageImpl<>(List.of(mockTicket, mockTicket2));
        when(repo.findByUserId(1L, PageRequest.of(1, 5))).thenReturn(mockPage);

        // Act
        var result = service.getBookedTicketsForUser(1, 5, 1);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("should get all tickets for an event")
    void getBookedTicketsForEvent() {
        // Arrange
        var mockTicket = new Ticket(1L, 1L, Ticket.Category.BAR, 1);
        var mockTicket2 = new Ticket(1L, 1L, Ticket.Category.BAR, 2);
        var mockPage = new PageImpl<>(List.of(mockTicket, mockTicket2));
        when(repo.findByEventId(1L, PageRequest.of(1, 5))).thenReturn(mockPage);

        // Act
        var result = service.getBookedTicketsForEvent(1, 5, 1);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("should cancel a ticket")
    void cancelTicket() {
        // Arrange
        var mockTicket = new Ticket(1L, 1L, Ticket.Category.BAR, 1);
        when(repo.findById(1L)).thenReturn(Optional.of(mockTicket));

        // Act
        var result = service.cancelTicket(1);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("should not cancel a ticket that does not exist")
    void cancelTicket_False() {
        // Arrange
        when(repo.findById(2L)).thenReturn(Optional.empty());

        // Act
        var result = service.cancelTicket(2);

        // Assert
        assertFalse(result);
    }
}
