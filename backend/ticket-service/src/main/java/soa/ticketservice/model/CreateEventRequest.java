package soa.ticketservice.model;

import soa.ticketservice.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Модель запрса на саоздание Event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateEventRequest {
    private String name;
    private String date;
    private Integer minAge;
    private EventType eventType;
    private Long id;
}
