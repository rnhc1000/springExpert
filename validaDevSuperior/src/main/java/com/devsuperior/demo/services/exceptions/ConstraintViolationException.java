package com.devsuperior.demo.services.exceptions;

import java.awt.*;

public class ConstraintViolationException extends RuntimeException{
  public ConstraintViolationException(String message) {
    super(message);
  }
}
