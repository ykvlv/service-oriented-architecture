package soa.ticketservice.controller;

import soa.ticketservice.common.Endpoints;
import soa.ticketservice.error.ErrorDescriptions;
import soa.ticketservice.model.CreateTicketRequest;
import soa.ticketservice.model.Ticket;
import soa.ticketservice.model.TicketDto;
import soa.ticketservice.model.common.NameRequest;
import soa.ticketservice.model.enums.TicketType;
import soa.ticketservice.repository.FilterCriteria;
import soa.ticketservice.repository.SortCriteria;
import soa.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping(value = Endpoints.CREATE_TICKET)
    public ResponseEntity<TicketDto> createTicket(
            @RequestBody CreateTicketRequest request
    ) {
        TicketDto ticketDto = ticketService.createTicket(request);
        return new ResponseEntity<>(ticketDto, HttpStatus.OK);
    }

    /**
     * Получение списка билетов.
     *
     * @return список билетов.
     */
    @GetMapping(value = Endpoints.GET_ALL_TICKETS)
    public ResponseEntity<List<TicketDto>> getAllTickets(
            @RequestParam(value = "filter", required = false) String[] filter,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset
    ) throws Exception {
        if (limit != null) {
            if (limit <= 0) {
                throw ErrorDescriptions.INCORRECT_LIMIT.exception();
            }
        }

        if (offset != null) {
            if (offset < 0) {
                throw ErrorDescriptions.INCORRECT_OFFSET.exception();
            }
        }

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        var allowedFilters = List.of(
                "id",
                "name",
                "coordinateX",
                "coordinateY",
                "creationDate",
                "price",
                "discount",
                "type"
        );

        List<FilterCriteria> filters = new ArrayList<>();
        if (filter != null){
            try {
                for (String f : filter) {
                    var key = f.split("\\[", 2)[0];
                    if (!allowedFilters.contains(key)) {
                        throw new Exception("Недопустимое значение фильтра " + key + ", должно быть одно иззначений: " + allowedFilters);
                    }

                    var val = f.split("\\]", 2)[1];
                    val = val.substring(1);
                    if (key.equals("type")){
                        val = val.toUpperCase();
                        try {
                            TicketType.valueOf(val);
                        } catch (Exception exc) {
                            throw new Exception("Недопустимое значение type: должно быть одно из значений: [CHEAP, BUDGETARY, USUAL, VIP]");
                        }
                    } else if (key.equals("creationDate")) {
                        try {
                            LocalDate date = LocalDate.parse(val);
                            System.out.println(date);
                        } catch (Exception e) {
                            throw new Exception("Недопустимое значение creationDate: ожидается LocalDate");
                        }
                    }

                    var op = f.split("\\[", 2)[1].split("\\]", 2)[0];

                    filters.add(
                            new FilterCriteria(
                                    key,
                                    op,
                                    key.equals("type") ? TicketType.valueOf(val) : key.equals("creationDate") ? LocalDate.parse(val) : val
                            )
                    );
                }
            } catch (IndexOutOfBoundsException exc){
                throw ErrorDescriptions.INCORRECT_FILTER.exception();
            }
        }

        List<SortCriteria> sc = new ArrayList<>();
        if (sort != null) {
            try {
                var listSorts = Arrays.asList(sort.split(","));
                for (String oneSort : listSorts) {
                    var descSort = oneSort.charAt(0) == '-';
                    var key = "";
                    if (descSort) {
                        key = oneSort.substring(1);
                    } else {
                        key = oneSort;
                    }
                    sc.add(new SortCriteria(key, !descSort));
                }
            } catch (Exception e) {
                throw ErrorDescriptions.INCORRECT_SORT.exception();
            }
        }
        List<TicketDto> tickets = ticketService.getAllTickets(filters, sc, limit, offset);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    /**
     * Получение билет по id.
     *
     * @return список кораблей.
     */
    @GetMapping(value = Endpoints.GET_TICKET_BY_ID)
    public ResponseEntity<TicketDto> getTicketById(
            @PathVariable("ticketId") Long ticketId
    ) {
        return new ResponseEntity<>(ticketService.getTicketById(ticketId), HttpStatus.OK);
    }

    /**
     * Удаление билета по id.
     */
    @DeleteMapping(value = Endpoints.DELETE_TICKET_BY_ID)
    public ResponseEntity<Object> deleteTicketById(
            @PathVariable("ticketId") Long ticketId
    ) {
        ticketService.deleteTicketById(ticketId);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    /**
     * Обновление билета по id.
     */
    @PutMapping(value = Endpoints.UPDATE_TICKET_BY_ID)
    public ResponseEntity<Object> updateTicketById(
            @PathVariable("ticketId") Long ticketId,
            @RequestBody CreateTicketRequest request
    ) {
        var res = ticketService.updateTicketById(ticketId, request);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    // front needs this!!!!!!!!
    @GetMapping(value = Endpoints.GET_TICKETS_COUNT)
    public ResponseEntity<Long> countTickets() {
        return new ResponseEntity<>(ticketService.countTickets(), HttpStatus.OK);
    }


    @GetMapping(value = Endpoints.GET_TICKETS_TYPES)
    public ResponseEntity<List<Object>> eventsTypes() {
        return new ResponseEntity<>(ticketService.getTypes(), HttpStatus.OK);
    }


//- [] GET /tickets/discount/sum
    @GetMapping(value = Endpoints.GET_TICKETS_DISCOUNT_SUM)
    public ResponseEntity<Ticket> getSumOfDiscount() {
        return new ResponseEntity<>(ticketService.findTicketWithMinType(), HttpStatus.OK);
    }

//-  GET /tickets/discount/count
    @GetMapping(value = Endpoints.GET_TICKETS_DISCOUNT_COUNT)
    public ResponseEntity<Object> getSumOfDiscountCount() {
        return new ResponseEntity<>(ticketService.sumOfDiscountCount(), HttpStatus.OK);
    }

//- [] GET /tickets/type/count

    @GetMapping(value = Endpoints.GET_TICKETS_UNIQUE_TYPES)
    public ResponseEntity<Object> getTicketsUniqueTypes() {
        return new ResponseEntity<>(ticketService.getTicketsUniqueTypes(), HttpStatus.OK);
    }

    @PostMapping(value = Endpoints.NEW_VIP_TICKET_BY_ID)
    public ResponseEntity<Object> createVipTicketById(
            @PathVariable("ticketId") Long ticketId
    ) {
        var res = ticketService.newVipTicketById(ticketId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(value = Endpoints.DISCOUNT_TICKET_BY_ID)
    public ResponseEntity<Object> createDiscountTicketById(
            @PathVariable("ticketId") Long ticketId,
            @PathVariable("discount") Double discount
    ) {
        var res = ticketService.newDiscountTicketById(ticketId, discount);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = Endpoints.FIND_TICKET_WITH_MIN_TYPE)
    public ResponseEntity<Object> findTicketWithMinType() {
        return new ResponseEntity<>(ticketService.findTicketWithMinType(), HttpStatus.OK);
    }

    @GetMapping(value = Endpoints.GET_TICKETS_WITH_NAME_CONTAINING_SUBSTRING)
    public ResponseEntity<Object> getTicketsWithNameContainingSubstring(
            @RequestParam(name = "name") String name
    ) {
        return new ResponseEntity<>(ticketService.getTicketsWithNameContainingSubstring(name), HttpStatus.OK);
    }
}
