package soa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;
import soa.ejb.model.ErrorDTO;


@ControllerAdvice
@ResponseBody
public class OrgdirectoryErrorHandler {
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleRestClientException(RestClientException ex) {
		return new ErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.name(), "Cannot connect to Organizations Service");
    }
}
