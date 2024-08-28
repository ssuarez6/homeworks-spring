package com.epam.homework2.dao;

import com.epam.homework2.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Repository
public class TicketDao {
    private InMemoryStorage tickets;

    @Autowired
    public void setTickets(InMemoryStorage storage){
        this.tickets = storage;
    }

    public Ticket findById(Long id) {
        return tickets.getTicketsMap().get(id);
    }

    public void save(Ticket ticket) {
        tickets.getTicketsMap().put(ticket.id(), ticket);
    }

    public List<Ticket> getAll(){
        return tickets.getTicketsMap().values().stream().toList();
    }

    public List<Ticket> getFilteredTickets(Predicate<Ticket> predicate, int pageSize, int pageNum) {
        var filtered = tickets.getTicketsMap().values().stream().filter(predicate).toList();

        int fromIndex = (pageNum -1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filtered.size());

        return filtered.subList(fromIndex, toIndex);
    }

    public boolean delete(Long ticketId) {
        if(tickets.getTicketsMap().containsKey(ticketId)) {
            tickets.getTicketsMap().remove(ticketId);
            return true;
        } else return false;
    }
}
