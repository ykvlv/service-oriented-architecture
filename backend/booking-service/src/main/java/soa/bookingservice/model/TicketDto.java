package soa.bookingservice.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class TicketDto {
    @JsonIgnoreProperties
    private Integer id;
    private String name;
    private CoordinatesDto coordinates;
    private LocalDate creationDate;
    private Long price;
    private Double discount;
    private TicketType type;
    private Event discipline;
}
