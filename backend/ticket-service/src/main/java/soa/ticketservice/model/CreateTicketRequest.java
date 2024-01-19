package soa.ticketservice.model;

import soa.ticketservice.model.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Модель запрса на саоздание Ticket.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateTicketRequest {
    private String name;
    private Coordinates coordinates;
    private Integer price;
    private Double discount;
    private TicketType type;
    private EventDto event;
    private Long id;
    private LocalDate creationDate;
}
