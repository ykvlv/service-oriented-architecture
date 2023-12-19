package soa.ticketservice.error;

import soa.ticketservice.model.common.ErrorResponse;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public ApplicationException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

}
