package com.epam.homework.jms;

import java.io.Serializable;

public class TicketMessage implements Serializable {
    private String userId;
    private String eventId;
    private String place;
    private String category;

    public TicketMessage() {
    }

    public TicketMessage(String userId, String eventId, String place, String category) {
        this.userId = userId;
        this.eventId = eventId;
        this.place = place;
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
