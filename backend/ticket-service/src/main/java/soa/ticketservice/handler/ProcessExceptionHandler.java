package soa.ticketservice.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import soa.ticketservice.controller.EventController;
import soa.ticketservice.controller.TicketController;
import soa.ticketservice.error.ApplicationException;
import soa.ticketservice.exception.EntityNotFoundException;
import soa.ticketservice.exception.NotValidParamsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import soa.ticketservice.model.common.ErrorResponse;

import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(assignableTypes = {TicketController.class, EventController.class})
public class ProcessExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
        System.out.println(e.getClass());
        return ResponseEntity.status(400)
                .body(ErrorResponse.of(400, "Непредвиденная ошибка"));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleException(MethodArgumentTypeMismatchException e) {
        String msg;
        if (e.getCause() instanceof NumberFormatException) {
            msg = "Ожидался адекватный запрос по пути";
        } else {
            msg = "В GET-параметре передано слишком длинное число";
        }

        return ResponseEntity.status(400)
                .body(ErrorResponse.of(400, msg));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> handleException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(405)
                .body(ErrorResponse.of(405,  "Такого метода нет"));
    }

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<?> handleException(ApplicationException e) {
        return ResponseEntity.status(400)
                .body(ErrorResponse.of(400, e.getMessage()));
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<?> handleInvalidFormatException(InvalidFormatException e) {
        return ResponseEntity.status(400)
                .body(ErrorResponse.of(400, "Ошибка: неверный формат числа "));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ErrorResponse.of(404, "Ошибка: сущность не найдена"));
    }

    @ExceptionHandler({NotValidParamsException.class})
    public ResponseEntity<?> handleNotValidParamsException(NotValidParamsException e) {
        return ResponseEntity.status(422)
                .body(ErrorResponse.of(422, "Ошибка: невалидный параметр"));
    }

    @ExceptionHandler({DateTimeParseException.class})
    public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.status(422)
                .body(ErrorResponse.of(422, "Ошибка: неправильная дата"));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String msg;
        if (e.getCause() instanceof UnrecognizedPropertyException) {
            msg = "лишнее поле '" + ((UnrecognizedPropertyException) e.getCause()).getPropertyName() + "'";
        } else if (e.getCause() instanceof InvalidFormatException) {
            msg = "зачем ты это сюда пишешь '" + ((InvalidFormatException) e.getCause()).getValue() + "', напиши что требуется.";
        } else if (e.getCause() instanceof MismatchedInputException) {
            var cs = (MismatchedInputException) e.getCause();
            msg = "Требуемый формат чисел не совпадает, требовался " + cs.getTargetType().getSimpleName();
        } else {
            msg = e.getMessage().split(";")[0];
        }

        return ResponseEntity.status(422)
                .body(ErrorResponse.of(422, "Невалидный формат json: " + msg));
    }


    @ExceptionHandler({ConstraintViolationException.class, BindException.class, MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleValidationException(Exception ex) {
        String message;
        if (ex instanceof MethodArgumentNotValidException) {
            message = "Некорректные параметры " + getValidationError((MethodArgumentNotValidException) ex);
        } else if (ex.getCause() instanceof IllegalArgumentException) {
            message = "Ошибка, пустая строка, че то ты не доложил";
        } else {
            message = ex.getMessage();
        }
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(400, message));
    }

    private String getValidationError(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(";"));
    }
}
