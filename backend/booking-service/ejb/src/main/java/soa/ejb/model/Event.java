package soa.ejb.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@Accessors(chain = true)
public class Event implements Serializable {
    @JsonIgnoreProperties
    private Long id;
    private String name;
    private LocalDate date;
    private Long minAge;
    private EventType eventType;
}
