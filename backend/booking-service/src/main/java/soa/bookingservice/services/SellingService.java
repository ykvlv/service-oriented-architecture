package soa.bookingservice.services;

import soa.bookingservice.model.common.ErrorResponse;
import soa.bookingservice.utils.RestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class SellingService {
    private final RestClient restClient;

    public SellingService(RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<?> increaseStepsCount(Integer ticketId, Integer personId) {
        try {
            return restClient.createVipTicket(ticketId, personId);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(400).body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(422).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            ErrorResponse err = new ErrorResponse();
            err.setCode(504);
            err.setMessage("Непредвиденная ошибка");
            return ResponseEntity.status(504).body(err);
        }
    }

    public ResponseEntity<?> makeDiscount(Integer ticketId, Integer personId, Double discount) {
        try {
            return restClient.makeDiscountTicket(ticketId, personId, discount);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(400).body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(422).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            ErrorResponse err = ErrorResponse.of(504, "Непредвиденная ошибка");
            return ResponseEntity.status(504).body(err);
        }
    }

}
