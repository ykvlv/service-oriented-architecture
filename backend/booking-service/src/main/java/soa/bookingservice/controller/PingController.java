package soa.bookingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import soa.bookingservice.catalog.Coordinates;
import soa.bookingservice.catalog.CreateVipTicketRequest;
import soa.bookingservice.catalog.CreateVipTicketResponse;
import soa.bookingservice.catalog.Event;
import soa.bookingservice.catalog.GetPingRequest;
import soa.bookingservice.catalog.GetPingResponse;
import soa.bookingservice.catalog.MakeDiscountTicketRequest;
import soa.bookingservice.catalog.MakeDiscountTicketResponse;
import soa.bookingservice.catalog.PingGetResponseDto;
import soa.bookingservice.catalog.Ticket;
import soa.bookingservice.model.CoordinatesDto;
import soa.bookingservice.model.EventDto;
import soa.bookingservice.model.TicketDto;
import soa.bookingservice.service.ClientService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


@Endpoint
public class PingController {
    private static final String NAMESPACE_URI = "http://soa/bookingservice/catalog";

    private ClientService clientService;

    @Autowired
    public PingController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPingRequest")
    @ResponsePayload
    public GetPingResponse getPing(@RequestPayload GetPingRequest request){
        Long id = request.getId();

        System.out.println("id = " + id);

        GetPingResponse response = new GetPingResponse();
        PingGetResponseDto responsePing = new PingGetResponseDto();
        responsePing.setId(id);
        response.setPing(responsePing);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createVipTicketRequest")
    @ResponsePayload
    public CreateVipTicketResponse createVipTicket(@RequestPayload CreateVipTicketRequest request) throws DatatypeConfigurationException {
        TicketDto ticketDto = clientService.createVipTicket(
                request.getTicketId(), request.getPersonId()
        );

        CreateVipTicketResponse response = new CreateVipTicketResponse();
        response.setTicket(fillTicket(ticketDto));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "makeDiscountTicketRequest")
    @ResponsePayload
    public MakeDiscountTicketResponse makeDiscountTicket(@RequestPayload MakeDiscountTicketRequest request) throws DatatypeConfigurationException {
        TicketDto ticketDto = clientService.makeDiscountTicket(
                request.getTicketId(), request.getPersonId(), request.getDiscount()
        );


        MakeDiscountTicketResponse response = new MakeDiscountTicketResponse();
        response.setTicket(fillTicket(ticketDto));
        return response;
    }


    private Ticket fillTicket(TicketDto ticketDto) throws DatatypeConfigurationException {
        Ticket ticket = new Ticket();
        ticket.setId(ticketDto.getId());
        ticket.setName(ticketDto.getName());

        XMLGregorianCalendar xmlGregorianCalendar =
                DatatypeFactory.newInstance().newXMLGregorianCalendar(ticketDto.getCreationDate().toString());
        ticket.setCreationDate(xmlGregorianCalendar);

        ticket.setPrice(ticketDto.getPrice());
        ticket.setDiscount(ticketDto.getDiscount());
        ticket.setTicketType(String.valueOf(ticketDto.getType()));

        CoordinatesDto coordinatesDto = ticketDto.getCoordinates();
        Coordinates coordinates = new Coordinates();
        if (coordinatesDto != null) {
            coordinates.setX(coordinatesDto.getX());
            coordinates.setY(coordinates.getY());
        }
        ticket.setCoordinates(coordinates);

        EventDto eventDto = ticketDto.getEventDto();
        Event event = new Event();
        if (eventDto != null) {
            event.setId(eventDto.getId());
            event.setName(eventDto.getName());

            XMLGregorianCalendar xmlGregorianCalendar2 =
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(eventDto.getDate().toString());
            event.setDate(xmlGregorianCalendar2);

            event.setMinAge(eventDto.getMinAge());
            event.setEventType(String.valueOf(eventDto.getEventType()));
        }
        ticket.setEvent(event);

        return ticket;
    }
}
