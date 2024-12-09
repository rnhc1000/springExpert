package com.devsuperior.demo.resources.handlers;

import com.devsuperior.demo.dto.ErrorResponseDTO;
import com.devsuperior.demo.services.exceptions.ConstraintViolationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String ILLEGAL_DATABASE_REQUEST = "Illegal database request!";
  private static final String CITY_NOT_REGISTERED = "Register this City before assigning it an event!";


  @ExceptionHandler(value= ConstraintViolationException.class)
  public ResponseEntity<ErrorResponseDTO> cityNameNullOrBlank(final ConstraintViolationException exception, HttpServletRequest request) {
    ErrorResponseDTO errorResponseDto = new ErrorResponseDTO(
        HttpStatus.UNPROCESSABLE_ENTITY.value(),
        exception.getMessage(),
        ResourceExceptionHandler.ILLEGAL_DATABASE_REQUEST,
        request.getRequestURI(),
        Instant.now()
    );

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY.value()).body(errorResponseDto);
  }

  @ExceptionHandler(value = IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDTO> cityNameNotRegistered(final IllegalArgumentException exception, HttpServletRequest request) {
    ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
    HttpStatus.UNPROCESSABLE_ENTITY.value(),
        exception.getMessage(),
        ResourceExceptionHandler.CITY_NOT_REGISTERED,
        request.getRequestURI(),
        Instant.now()
    );
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY.value()).body(errorResponseDTO);
  }

}
