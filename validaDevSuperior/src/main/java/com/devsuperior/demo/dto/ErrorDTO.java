package com.devsuperior.demo.dto;

import com.devsuperior.demo.resources.handlers.FieldMessage;

import java.util.ArrayList;
import java.util.List;

public class ErrorDTO {

  private final List<FieldMessage> errors = new ArrayList<>();

  public List<FieldMessage> getErrors() {
    return errors;
  }

  public void addError(String fieldName, String message) {

    errors.add(new FieldMessage(fieldName, message));
  }
}
