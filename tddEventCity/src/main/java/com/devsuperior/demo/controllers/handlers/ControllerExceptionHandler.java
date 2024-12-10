package com.devsuperior.demo.controllers.handlers;

import com.devsuperior.demo.dto.ErrorResponseDTO;
import com.devsuperior.demo.service.exceptions.ConstraintViolationException;
import com.devsuperior.demo.service.exceptions.DataIntegrityViolationException;
import com.devsuperior.demo.service.exceptions.IllegalArgumentException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
  private static final String ARGUMENT_NOT_VALID = "Entity Not Found in DB";
  private final static String CITY_CANNOT_BE_DELETED = "Entity City can not be deleted";
  private static final String ILLEGAL_DATABASE_REQUEST = "Illegal database request!";

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDTO> illegalArguments(IllegalArgumentException exception, HttpServletRequest request) {

    ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
        HttpStatus.UNPROCESSABLE_ENTITY.value(),
        exception.getMessage(),
        ControllerExceptionHandler.ARGUMENT_NOT_VALID,
        request.getRequestURI(),
        Instant.now()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorResponseDTO);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponseDTO> violationException(final ConstraintViolationException exception, HttpServletRequest request) {
    ErrorResponseDTO errorResponseDto = new ErrorResponseDTO(
        HttpStatus.BAD_REQUEST.value(),
        exception.getMessage(),
        ControllerExceptionHandler.ILLEGAL_DATABASE_REQUEST,
        request.getRequestURI(),
        Instant.now()
    );
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY.value()).body(errorResponseDto);

  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponseDTO> integrityViolation(DataIntegrityViolationException exception, HttpServletRequest request) {

    ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
        HttpStatus.UNPROCESSABLE_ENTITY.value(),
        exception.getMessage(),
        ControllerExceptionHandler.CITY_CANNOT_BE_DELETED,
        request.getRequestURI(),
        Instant.now()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResponseDTO);

  }
}
