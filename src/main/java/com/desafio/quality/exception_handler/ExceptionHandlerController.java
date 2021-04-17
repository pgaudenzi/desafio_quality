package com.desafio.quality.exception_handler;

import com.desafio.quality.dtos.ErrorDto;
import com.desafio.quality.exceptions.IllegalDateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

/**
 * Manage all the exceptions
 */
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(IllegalDateException.class)
    public ResponseEntity<ErrorDto> handleIllegalDate(IllegalDateException e) {
        ErrorDto error = new ErrorDto("IllegalDateException", e.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorDto> handleIllegalDate(DateTimeParseException e) {
        ErrorDto error = new ErrorDto("DateTimeParseException", "Date format must be dd/mm/yyyy",
                HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointer(NullPointerException e) {
        ErrorDto error = new ErrorDto("NullPointerException", "Please, review the request's data",
                HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(error, error.getStatus());
    }

}
