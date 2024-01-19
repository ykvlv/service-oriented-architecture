package soa.ejb.service;

import soa.ejb.model.TicketDto;

public interface RestClientService {

    TicketDto createVipTicket(Integer ticketId, Integer personId);
    TicketDto makeDiscountTicket(Integer ticketId, Integer personId, Double discount);
}
