package soa.ticketservice.model;

import soa.ticketservice.model.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Модель запрса на саоздание Ticket.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateTicketRequest {
    private String name;
    private Coordinates coordinates;
    private Double price;
    private Double discount;
    private Boolean refundable;
    private TicketType type;
    private EventDto event;
    private Long id;
    private Date creationDate;

    public void setRefundable(Object value) throws Exception {
        if (value instanceof Boolean) {
            refundable = (Boolean) value;
        } else {
            throw new Exception("refundable only boolean");
        }
    }

}
