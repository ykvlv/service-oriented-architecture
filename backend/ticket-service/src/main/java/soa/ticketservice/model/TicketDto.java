package soa.ticketservice.model;

import soa.ticketservice.model.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TicketDto {
    private Long id;
    private String name;
    private Coordinates coordinates;
    private String creationDate;
    private Integer price;
    private Double discount;
    private TicketType type;
    private EventDto event;
}

