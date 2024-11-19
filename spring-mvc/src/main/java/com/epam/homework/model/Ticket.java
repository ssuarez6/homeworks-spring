package com.epam.homework.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long eventId;
  private Long userId;

  @Enumerated(EnumType.STRING)
  private Category category;

  private int place;

  public Ticket() {}

  public Ticket(Long eventId, Long userId, Category category, int place) {
    this.eventId = eventId;
    this.userId = userId;
    this.category = category;
    this.place = place;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public int getPlace() {
    return place;
  }

  public void setPlace(int place) {
    this.place = place;
  }

  public enum Category {
    STANDARD,
    PREMIUM,
    BAR
  }
}
