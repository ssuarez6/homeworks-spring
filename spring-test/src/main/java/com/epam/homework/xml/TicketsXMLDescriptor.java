package com.epam.homework.xml;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "tickets")
public class TicketsXMLDescriptor {

    private List<TicketXMLDescriptor> ticketsXML;

    @XmlElement(name = "ticket")
    public List<TicketXMLDescriptor> getTicketsXML() {
        return ticketsXML;
    }

    public void setTicketsXML(List<TicketXMLDescriptor> ticketsXML) {
        this.ticketsXML = ticketsXML;
    }

}
