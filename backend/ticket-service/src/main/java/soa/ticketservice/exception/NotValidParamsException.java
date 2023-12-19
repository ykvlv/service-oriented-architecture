package soa.ticketservice.exception;

public class NotValidParamsException extends RuntimeException {
    public NotValidParamsException(String message) {
        super(message);
    }
}
