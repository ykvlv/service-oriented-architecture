package soa.bookingservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soa.bookingservice.service.ClientService;

@RestController
@RequestMapping("booking/sell")
public class PingTroller {
    private final ClientService clientService;

    public PingTroller(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping(value = "/vip/{ticket-id}/{person-id}")
    public ResponseEntity<?> increaseStepsCount(@PathVariable("ticket-id") Integer ticketId, @PathVariable("person-id") Integer personId) {
        return ResponseEntity.ok(clientService.createVipTicket(ticketId, personId));
    }

    @PostMapping(value = "/discount/{ticket-id}/{person-id}/{discount}")
    public ResponseEntity<?> makeHardcore(
            @PathVariable("ticket-id") Integer ticketId,
            @PathVariable("person-id") Integer personId,
            @PathVariable("discount") Double discount
    ) {
        return ResponseEntity.ok(clientService.makeDiscountTicket(ticketId, personId, discount));
    }
}
