package com.devsuperior.demo.service.exceptions;

public class DataIntegrityViolationException extends RuntimeException{
  public DataIntegrityViolationException(String message) {
    super(message);
  }
}
