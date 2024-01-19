package soa.ejb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class TicketDto implements Serializable {
    @JsonIgnoreProperties
    private Integer id;
    private String name;
    private CoordinatesDto coordinates;
    private String creationDate;
    private Integer price;
    private Double discount;
    private TicketType type;
    private Event discipline;
}
