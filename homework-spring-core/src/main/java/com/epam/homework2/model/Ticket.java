package com.epam.homework2.model;

public record Ticket(long id, long eventId, long userId, Category category, int place) {
    public enum Category{
        STANDARD,
        PREMIUM,
        BAR
    }
}


