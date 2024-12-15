package com.devsuperior.demo.resources.handlers;

import com.devsuperior.demo.dto.ErrorResponseDTO;
import com.devsuperior.demo.services.exceptions.ConstraintViolationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

  private static final String CITY_NOT_REGISTERED = "Register this City before assigning it an event!";


  @ExceptionHandler(value = ConstraintViolationException.class)
  public ResponseEntity<ValidationError> cityNameNullOrBlank(final ConstraintViolationException exception, HttpServletRequest request) {
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    ValidationError error = new ValidationError();
    error.setTimestamp(Instant.now());
    error.setStatus(status.value());
    error.setError("Validation exception");
    error.setMessage(exception.getMessage());
    error.setPath(request.getRequestURI());
    error.addError("name", "Campo requerido");

    return ResponseEntity.status(status).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    ValidationError errors = new ValidationError();
    errors.setTimestamp(Instant.now());
    errors.setStatus(status.value());
    errors.setError("Validation exception");
    errors.setMessage(e.getMessage());
    errors.setPath(request.getRequestURI());

    for (FieldError f : e.getBindingResult().getFieldErrors()) {
      errors.addError(f.getField(), f.getDefaultMessage());
    }

    return ResponseEntity.status(status).body(errors);
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
