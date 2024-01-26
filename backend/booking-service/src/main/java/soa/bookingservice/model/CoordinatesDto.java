package soa.bookingservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Setter
@Getter
@Accessors(chain = true)
public class CoordinatesDto implements Serializable {
    private Long x;
    private Integer y;
}
