package com.devsuperior.demo.services.exceptions;


public class ConstraintViolationException extends RuntimeException{
  public ConstraintViolationException(String message) {
    super(message);
  }
}
