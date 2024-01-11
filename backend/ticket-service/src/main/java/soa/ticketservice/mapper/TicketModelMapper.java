package soa.ticketservice.mapper;


import soa.ticketservice.model.Coordinates;
import soa.ticketservice.model.EventDto;
import soa.ticketservice.model.Ticket;
import soa.ticketservice.model.TicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;


@Component
@RequiredArgsConstructor
public class TicketModelMapper {
    public TicketDto map(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setName(ticket.getName());
        dto.setCoordinates(Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()));
        dto.setCreationDate(ticket.getCreationDate().atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
        dto.setPrice(ticket.getPrice());
        dto.setDiscount(ticket.getDiscount());
        dto.setType(ticket.getType());
        if(ticket.getEvent() != null){
            dto.setEvent(EventDto.of(
                    ticket.getEvent().getId(),
                    ticket.getEvent().getName(),
                    ticket.getEvent().getDate() == null ? null :
                    ticket.getEvent().getDate().atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")),
                    ticket.getEvent().getMinAge(),
                    ticket.getEvent().getEventType()
            ));
        }
        return dto;
    }
}
