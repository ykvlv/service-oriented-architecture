package soa.ticketservice.service;

import soa.ticketservice.mapper.EventModelMapper;
import soa.ticketservice.model.Event;
import soa.ticketservice.model.CreateEventRequest;
import soa.ticketservice.model.EventDto;
import soa.ticketservice.model.enums.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import soa.ticketservice.error.ErrorDescriptions;
import soa.ticketservice.repository.EventRepository;
import soa.ticketservice.repository.EventSpecification;
import soa.ticketservice.repository.FilterCriteria;
import soa.ticketservice.repository.SortCriteria;
import soa.ticketservice.repository.TicketRepository;


import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    /**
     * {@link EventRepository}.
     */
    private final TicketRepository ticketRepository;


    /**
     * {@link EventModelMapper}.
     */
    private final EventModelMapper eventModelMapper;

    @Override
    public EventDto createEvent(CreateEventRequest request) {
        Event event = new Event();
        event.setName(request.getName());
        event.setDate(request.getDate());
        event.setMinAge(request.getMinAge());
        event.setEventType(request.getEventType());
        eventRepository.save(event);
        EventDto createdEvent = new EventDto();
        createdEvent.setId(event.getId());
        createdEvent.setName(event.getName());
        createdEvent.setDate(event.getDate() == null ? null : event.getDate().atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
        createdEvent.setMinAge(event.getMinAge());
        createdEvent.setEventType(event.getEventType());
        return createdEvent;
    }

    @Override
    public List<EventDto> getAllEvents(List<FilterCriteria> filterBy, List<SortCriteria> sortBy, Long limit, Long offset) throws Exception {
        for (var e : filterBy){
            System.out.println(e);
        }
        System.out.println(sortBy);

        try {
            EventSpecification spec = new EventSpecification(filterBy);
            var eventsStream = eventRepository.findAll(spec).stream();

            for (var f :filterBy){
                if (f.getKey().equals("date")){
					eventsStream = switch (f.getOperation()) {
						case "eq" -> eventsStream.filter(event -> {
                            if (event.getDate() == null) return false;
                            Long curdate = event.getDate().toEpochDay();
                            Long needdate = ((LocalDate) f.getValue()).toEpochDay();

                            var eq = curdate.compareTo(needdate);
                            return eq == 0;
                        });
						case "ne" -> eventsStream.filter(event -> {
                            if (event.getDate() == null) return false;
                            Long curdate = event.getDate().toEpochDay();
                            Long needdate = ((LocalDate) f.getValue()).toEpochDay();

                            var eq = curdate.compareTo(needdate);
                            return eq != 0;
                        });
						case "gt" -> eventsStream.filter(event -> {
                            if (event.getDate() == null) return false;
                            Long curdate = event.getDate().toEpochDay();
                            Long needdate = ((LocalDate) f.getValue()).toEpochDay();

                            var eq = curdate.compareTo(needdate);
                            return eq > 0;
                        });
						default -> eventsStream.filter(event -> {
                            if (event.getDate() == null) return false;
                            Long curdate = event.getDate().toEpochDay();
                            Long needdate = ((LocalDate) f.getValue()).toEpochDay();

                            var eq = curdate.compareTo(needdate);
                            return eq < 0;
                        });
					};
                } else if (f.getKey().equals("eventType")){
                    EventType newS = (EventType) f.getValue();
                    if (newS.toString().equals("-")) {
                        continue;
                    }
                    if (f.getOperation().equals("eq")) {
                        eventsStream = eventsStream.filter(event -> event.getEventType().equals(f.getValue()));
                    } else if (f.getOperation().equals("ne")) {
                        eventsStream = eventsStream.filter(event -> !event.getEventType().equals(f.getValue()));
                    } else if (f.getOperation().equals("gt")) {
                        eventsStream = eventsStream.filter(event -> (event.getEventType().compareTo((EventType) f.getValue()) < 0));
                    } else {
                        eventsStream = eventsStream.filter(event -> (event.getEventType().compareTo((EventType) f.getValue()) > 0));
                    }
                } else if (f.getKey().equals("name")) {
                    String name = (String) f.getValue();
                    eventsStream = switch (f.getOperation()) {
//                        case "ne" -> eventsStream.filter(event -> !event.getDate().equals((Date) f.getValue()));
//                        case "gt" -> eventsStream.filter(event -> event.getDate().before((Date) f.getValue()));
//                        case "lt" -> eventsStream.filter(event -> event.getDate().after((Date) f.getValue()));
                        default -> eventsStream.filter(event -> event.getName().contains(name.subSequence(0, name.length())));
                    };
                }
            }

            if (sortBy != null && sortBy.size() != 0) {
                Comparator<Event> c = null;
                for (SortCriteria sortCriteria : sortBy) {
                    Comparator<Event> currentComp;
                    var desc = !sortCriteria.getAscending();
                    switch (sortCriteria.getKey()) {
                        case "id" -> currentComp = Comparator.comparing(Event::getId);
                        case "name" -> currentComp = Comparator.comparing(Event::getName);
                        case "date" -> currentComp = desc
                                ? Comparator.comparing(Event::getDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                                : Comparator.comparing(Event::getDate, Comparator.nullsLast(Comparator.naturalOrder()));
                        case "minAge" -> currentComp = Comparator.comparing(Event::getMinAge);
                        case "eventType" -> currentComp = Comparator.comparing(Event::getEventType);
                        default -> throw ErrorDescriptions.INCORRECT_SORT.exception();
                    }
                    if (desc) currentComp = currentComp.reversed();
                    if (c == null) {
                        c = currentComp;
                    } else {
                        c = c.thenComparing(currentComp);
                    }
                }
                if (c != null) eventsStream = eventsStream.sorted(c);
            }

            var es =  eventsStream
                    .skip(offset)
                    .limit(limit)
                    .map(eventModelMapper::map)
                    .collect(Collectors.toList());

            System.out.println(es);
            return es;
        } catch (
            InvalidDataAccessApiUsageException exc){
                throw new Exception("В фильтре передано недопустимое значение для сравнения");
        }
    }

    @Override
    public EventDto getEventById(Long eventId) {
        if (!eventRepository.existsById(eventId)){
            throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
        }

        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
        }
        return eventModelMapper.map(optionalEvent.get());
    }

    @Override
    @Transactional
    public void deleteEventById(Long eventId) {
        if (!eventRepository.existsById(eventId)){
            throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
        }
        ticketRepository.deleteAllByEventId(eventId);
        eventRepository.deleteById(eventId);
    }

    @Override
    public EventDto updateEventById(Long eventId, CreateEventRequest request) {
        if (!eventRepository.existsById(eventId)) {
            throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
        }

        Event updatedEvent = new Event();
        updatedEvent.setId(eventId);
        updatedEvent.setName(request.getName());
        updatedEvent.setDate(request.getDate());
        updatedEvent.setMinAge(request.getMinAge());
        updatedEvent.setEventType(request.getEventType());

        eventRepository.save(updatedEvent);
        return eventModelMapper.map(updatedEvent);
    }

    @Override
    public long countEvents() {
        return eventRepository.count();
    }

    @Override
    public List<Object> getTypes() {
        var res = new ArrayList<>();
        for (var type : EventType.values()) {
            HashMap<String, String> a = new HashMap<>();
            a.put("value", type.name());
            a.put("desc", type.toString());
            res.add(a);
        }
        return res;
    }
}
