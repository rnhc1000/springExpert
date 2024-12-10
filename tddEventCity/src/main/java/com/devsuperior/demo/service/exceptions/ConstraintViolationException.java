package com.devsuperior.demo.service.exceptions;


public class ConstraintViolationException extends RuntimeException{
  public ConstraintViolationException(String message) {
    super(message);
  }
}
