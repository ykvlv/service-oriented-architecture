package soa.bookingservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CoordinatesDto {
    private Long x;
    private Integer y;
}
