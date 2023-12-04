package soa.ticketservice.service;


import soa.domain.model.CreateEventRequest;
import soa.domain.model.EventDto;
import soa.domain.repository.FilterCriteria;
import soa.domain.repository.SortCriteria;

import java.util.List;

public interface EventService {
    EventDto createEvent(CreateEventRequest request);

    List<EventDto> getAllEvents(List<FilterCriteria> filterBy, List<SortCriteria> sortBy, Long limit, Long offset) throws Exception;

    EventDto getEventById(Long ticketId);

    void deleteEventById(Long eventId);

    EventDto updateEventById(Long eventId, CreateEventRequest request);

    long countEvents();

    List<Object> getTypes();
}
