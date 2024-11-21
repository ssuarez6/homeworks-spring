package com.epam.homework.xml;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
    public ObjectFactory() {}

    public TicketXMLDescriptor createTicket() {
        return new TicketXMLDescriptor();
    }

    public TicketsXMLDescriptor createTickets() {
        return new TicketsXMLDescriptor();
    }
}
