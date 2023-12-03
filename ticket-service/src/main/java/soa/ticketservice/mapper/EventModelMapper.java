package soa.ticketservice.mapper;

import soa.ticketservice.model.Event;
import soa.ticketservice.model.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventModelMapper {
    public EventDto map(Event event) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDate(event.getDate());
        dto.setMinAge(event.getMinAge());
        dto.setEventType(event.getEventType());
        return dto;
    }

    public Event map(EventDto event) {
        Event dto = new Event();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDate(event.getDate());
        dto.setMinAge(event.getMinAge());
        dto.setEventType(event.getEventType());
        return dto;
    }
}
