package soa.ticketservice.service;


import soa.ticketservice.model.CreateEventRequest;
import soa.ticketservice.model.EventDto;
import soa.ticketservice.repository.FilterCriteria;
import soa.ticketservice.repository.SortCriteria;

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
