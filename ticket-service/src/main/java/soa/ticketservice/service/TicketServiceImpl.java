package soa.ticketservice.service;

import soa.ticketservice.error.ErrorDescriptions;
import soa.ticketservice.mapper.TicketModelMapper;
import soa.ticketservice.model.*;
import soa.ticketservice.model.Coordinates;
import soa.ticketservice.model.CreateEventRequest;
import soa.ticketservice.model.CreateTicketRequest;
import soa.ticketservice.model.Event;
import soa.ticketservice.model.EventDto;
import soa.ticketservice.model.Ticket;
import soa.ticketservice.model.TicketDto;
import soa.ticketservice.model.enums.TicketType;
import soa.ticketservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import soa.ticketservice.mapper.EventModelMapper;
import soa.ticketservice.repository.EventRepository;
import soa.ticketservice.repository.FilterCriteria;
import soa.ticketservice.repository.SortCriteria;
import soa.ticketservice.repository.TicketRepository;
import soa.ticketservice.repository.TicketSpecification;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    private final EventRepository eventRepository;

    private final EventService eventService;

    private final TicketModelMapper ticketModelMapper;

    private final EventModelMapper eventModelMapper;

    @Override
    public TicketDto createTicket(CreateTicketRequest request) {
        if (request.getRefundable() == null) {
            throw ErrorDescriptions.REFUNDABLE_MUST_PRESENT.exception();
        }


        if (request.getCoordinates() == null ||
                request.getCoordinates().getX() == null ||
                request.getCoordinates().getY() == null
        ) {
            throw ErrorDescriptions.COORDINATES_MUST_PRESENT.exception();
        }

        if (request.getCoordinates().getX() <= -686L) {
            throw ErrorDescriptions.X_BAD.exception();
        }

        if (request.getDiscount() == null || request.getDiscount() < 1 || request.getDiscount() > 100) {
            throw ErrorDescriptions.DISCOUNT_MUST_PRESENT.exception();
        }


        Ticket ticket = new Ticket();
        ticket.setName(request.getName());
        ticket.setCoordinateX(request.getCoordinates().getX());
        ticket.setCoordinateY(request.getCoordinates().getY());
        ticket.setCreationDate(new Date());
        ticket.setPrice(request.getPrice());
        ticket.setDiscount(request.getDiscount());
        ticket.setRefundable(request.getRefundable());
        ticket.setType(request.getType());

        TicketDto createdTicket = new TicketDto();

        if (request.getEvent() != null) {
            if (request.getEvent().getId() != null) {
                if (!eventRepository.existsById(request.getEvent().getId())) {
                    throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
                }
                else {
                    Event event = eventRepository.findById(request.getEvent().getId()).get();
                    ticket.setEvent(event);
                    createdTicket.setEvent(eventModelMapper.map(event));
                }
            } else {  // create a new one
                EventDto newEvent = eventService.createEvent(CreateEventRequest.of(
                        request.getEvent().getName(),
                        request.getEvent().getDate(),
                        request.getEvent().getMinAge(),
                        request.getEvent().getEventType()
                ));
                System.out.println(newEvent.getId());
                Event event = eventRepository.findById(newEvent.getId()).get();
                ticket.setEvent(event);
                createdTicket.setEvent(eventModelMapper.map(event));
            }
        }
        ticketRepository.save(ticket);

        createdTicket.setId(ticket.getId());
        createdTicket.setName(ticket.getName());
        createdTicket.setCoordinates(Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()));
        createdTicket.setCreationDate(Date.from(ticket.getCreationDate().toInstant()));
        createdTicket.setPrice(ticket.getPrice());
        createdTicket.setDiscount(ticket.getDiscount());
        createdTicket.setRefundable(ticket.getRefundable());
        createdTicket.setType(ticket.getType());
        return createdTicket;
    }

    @Override
    public List<TicketDto> getAllTickets(
            List<FilterCriteria> filterBy, List<SortCriteria> sortBy, Long limit, Long offset
    ) throws Exception {
        for (var e : filterBy){
            System.out.println(e);
        }
        System.out.println(sortBy);

        try {
            TicketSpecification spec = new TicketSpecification(filterBy);
            var ticketsStream = ticketRepository.findAll(spec).stream();

            for (var f :filterBy){
                if (f.getKey().equals("creationDate")){
                    if (f.getOperation().equals("eq")) {
                        ticketsStream =ticketsStream.filter(ticket -> ticket.getCreationDate().equals((Date)f.getValue()));
                    } else if (f.getOperation().equals("ne")) {
                        ticketsStream =ticketsStream.filter(ticket -> !ticket.getCreationDate().equals((Date)f.getValue()));
                    } else if (f.getOperation().equals("gt")) {
                        ticketsStream =ticketsStream.filter(ticket -> ticket.getCreationDate().before((Date)f.getValue()));
                    } else {
                        ticketsStream =ticketsStream.filter(ticket -> ticket.getCreationDate().after((Date)f.getValue()));
                    }
                } else if (f.getKey().equals("type")){
                    if (f.getOperation().equals("eq")) {
                        ticketsStream = ticketsStream.filter(event -> event.getType().equals(f.getValue()));
                    } else if (f.getOperation().equals("ne")) {
                        ticketsStream = ticketsStream.filter(event -> !event.getType().equals(f.getValue()));
                    } else if (f.getOperation().equals("gt")) {
                        ticketsStream = ticketsStream.filter(event -> (event.getType().compareTo((TicketType) f.getValue()) < 0));
                    } else {
                        ticketsStream = ticketsStream.filter(event -> (event.getType().compareTo((TicketType) f.getValue()) > 0));
                    }
                }
            }

            if (sortBy != null && sortBy.size() != 0) {
                Comparator<Ticket> c = null;
                for (SortCriteria sortCriteria : sortBy) {
                    Comparator<Ticket> currentComp;
                    var desc = !sortCriteria.getAscending();
                    switch (sortCriteria.getKey()) {
                        case "id" -> currentComp = Comparator.comparing(Ticket::getId);
                        case "name" -> currentComp = Comparator.comparing(Ticket::getName);
                        case "coordinateX" -> currentComp = Comparator.comparing(Ticket::getCoordinateX);
                        case "coordinateY" -> currentComp = Comparator.comparing(Ticket::getCoordinateY);
                        case "creationDate" -> currentComp = Comparator.comparing(Ticket::getCreationDate);
                        case "price" -> currentComp = Comparator.comparing(Ticket::getPrice);
                        case "discount" -> currentComp = Comparator.comparing(Ticket::getDiscount);
                        default -> throw ErrorDescriptions.INCORRECT_SORT.exception();
                    }
                    if (desc) currentComp = currentComp.reversed();
                    if (c == null) {
                        c = currentComp;
                    } else {
                        c = c.thenComparing(currentComp);
                    }
                }
                if (c != null) ticketsStream = ticketsStream.sorted(c);
            }
            return ticketsStream
                    .skip(offset)
                    .limit(limit)
                   .map(ticketModelMapper::map)
                   .collect(Collectors.toList());
        } catch (InvalidDataAccessApiUsageException exc){
            throw new Exception("В фильтре передано недопустимое значение для сравнения");
        }
    }

    @Override
    public TicketDto getTicketById(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        Ticket ticket = ticketRepository.findById(ticketId).get();
        return ticketModelMapper.map(ticket);
    }

    @Override
    public TicketDto newVipTicketById(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        Ticket ticket = ticketRepository.findById(ticketId).get();
        System.out.println(ticket.getEvent());
        TicketDto newVipTicket = createTicket(CreateTicketRequest.of(
                ticket.getName(),
                Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()),
                ticket.getPrice() * 2,
                ticket.getDiscount(),
                ticket.getRefundable(),
                TicketType.VIP,
                eventModelMapper.map(ticket.getEvent())
        ));
        return newVipTicket;
    }

    @Override
    public TicketDto newDiscountTicketById(Long ticketId, Double discount) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        Ticket ticket = ticketRepository.findById(ticketId).get();
        System.out.println(ticket.getEvent());
        TicketDto newVipTicket = createTicket(CreateTicketRequest.of(
                ticket.getName(),
                Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()),
                ticket.getPrice() * (1 - discount / 100.0),
                discount,
                ticket.getRefundable(),
                ticket.getType(),
                eventModelMapper.map(ticket.getEvent())
        ));
        return newVipTicket;
    }

    @Override
    public void deleteTicketById(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)){
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        ticketRepository.deleteById(ticketId);
    }

    @Override
    public TicketDto updateTicketById(Long ticketId, CreateTicketRequest request) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }

        if (request.getRefundable() == null) {
            throw ErrorDescriptions.REFUNDABLE_MUST_PRESENT.exception();
        }

        if (request.getDiscount() == null || request.getDiscount() < 1 || request.getDiscount() > 100) {
            throw ErrorDescriptions.DISCOUNT_MUST_PRESENT.exception();
        }

        if (request.getCoordinates() == null ||
                request.getCoordinates().getX() == null ||
                request.getCoordinates().getY() == null
        ) {
            throw ErrorDescriptions.COORDINATES_MUST_PRESENT.exception();
        }

        if (request.getCoordinates().getX() <= -686L) {
            throw ErrorDescriptions.X_BAD.exception();
        }


        Ticket updatedTicket = new Ticket();
        updatedTicket.setId(ticketId);
        updatedTicket.setName(request.getName());
        updatedTicket.setCoordinateX(request.getCoordinates().getX());
        updatedTicket.setCoordinateY(request.getCoordinates().getY());
        updatedTicket.setCreationDate(new Date());
        updatedTicket.setPrice(request.getPrice());
        updatedTicket.setDiscount(request.getDiscount());
        updatedTicket.setRefundable(request.getRefundable());
        updatedTicket.setType(request.getType());

        TicketDto createdTicket = new TicketDto();

        if (request.getEvent() != null) {
            if (request.getEvent().getId() != null) {
                if (!eventRepository.existsById(request.getEvent().getId())) {
                    throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
                }
                else {
                    Event event = eventRepository.findById(request.getEvent().getId()).get();
                    updatedTicket.setEvent(event);
                    createdTicket.setEvent(eventModelMapper.map(event));
                }
            } else {  // create a new one
                EventDto newEvent = eventService.createEvent(CreateEventRequest.of(
                        request.getEvent().getName(),
                        request.getEvent().getDate(),
                        request.getEvent().getMinAge(),
                        request.getEvent().getEventType()
                ));
                System.out.println(newEvent.getId());
                Event event = eventRepository.findById(newEvent.getId()).get();
                updatedTicket.setEvent(event);
                createdTicket.setEvent(eventModelMapper.map(event));
            }
        }
        ticketRepository.save(updatedTicket);
        return ticketModelMapper.map(updatedTicket);
    }

    @Override
    public long countTickets() {
        return ticketRepository.count();
    }

    @Override
    public List<Object> getTypes() {
        var res = new ArrayList<>();
        for (var type : TicketType.values()) {
            HashMap<String, String> a = new HashMap<>();
            a.put("value", type.name());
            a.put("desc", type.toString());
            res.add(a);
        }
        return res;
    }

    @Override
    public Double sumOfDiscount() {
        var all = ticketRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false).map(Ticket::getDiscount).reduce((double) 0, Double::sum);
    }

    @Override
    public Object sumOfDiscountCount() {
        var all = ticketRepository.findAll();
//        [
//        {discount: 10
//         count: 100}
//        {discount: 11
//         count: 101}
//        ]
        var groupedByDiscount = StreamSupport.stream(all.spliterator(), false)
                .collect(groupingBy(
                        Ticket::getDiscount
                ));
        var res = new ArrayList<Map<String, Number>>();

        for(var t : groupedByDiscount.entrySet()){
            var hueta =
                    Map.of("discount", t.getKey(), "count", (Number) t.getValue().size());
            res.add(hueta);
        }
        return res;
    }

    @Override
    public Long getTicketsTypeCount(TicketType ticketType) {
        var all = ticketRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .filter(ticket -> ticket.getType().getValue() < ticketType.getValue())
                .count();
    }
}
