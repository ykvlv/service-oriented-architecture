package soa.bookingservice.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Setter
@Getter
@Accessors(chain = true)
public class Event {
    @JsonIgnoreProperties
    private Long id;
    private String name;
    private LocalDate date;
    private Long minAge;
    private EventType eventType;
}
