package soa.ticketservice.model;

import soa.ticketservice.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class EventDto {
    private Long id;

    @NotBlank(message = "Название дисциплины не должно быть пустым")
    @NotNull
    private String name;

    private String date;

    private Integer minAge;

    private EventType eventType;
}

