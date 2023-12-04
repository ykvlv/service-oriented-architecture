package soa.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import soa.domain.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Модель запрса на саоздание Event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateEventRequest {
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;
    private Integer minAge;
    private EventType eventType;
}
