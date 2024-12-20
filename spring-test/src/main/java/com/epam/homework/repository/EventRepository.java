package com.epam.homework.repository;

import com.epam.homework.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByTitle(String title, Pageable pageable);

    Page<Event> findByDate(Date date, Pageable pageable);
}
