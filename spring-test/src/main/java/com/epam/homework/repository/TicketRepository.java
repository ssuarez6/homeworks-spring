package com.epam.homework.repository;

import com.epam.homework.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(
            "SELECT t FROM Ticket t WHERE t.userId = :userId AND t.eventId = :eventId AND t.place = :place AND t.category = :category")
    Optional<Ticket> findBookedTicket(
            @Param("userId") Long userId,
            @Param("eventId") Long eventId,
            @Param("place") int place,
            @Param("category") Ticket.Category category);

    Page<Ticket> findByUserId(Long userId, Pageable pageable);

    Page<Ticket> findByEventId(Long eventId, Pageable pageable);
}
