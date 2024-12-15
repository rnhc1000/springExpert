package com.devsuperior.demo.dto;

import com.devsuperior.demo.entities.Event;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class EventDTO {
  private Long id;

  @NotBlank(message = "Campo requerido")
  private String name;

  @NotBlank(message = "Campo requerido")
  private String url;

  @FutureOrPresent(message = "A data do evento não pode ser passada")
  private LocalDate date;

  @NotNull(message = "Campo requerido")
  private Long cityId;

  public EventDTO(Long id, String name, String url, LocalDate date, Long cityId) {
    this.id = id;
    this.name = name;
    this.url = url;
    this.date = date;
    this.cityId = cityId;
  }

  public EventDTO(Long id, String name,  LocalDate date, String url, Long cityId) {
    this.id = id;
    this.name = name;
    this.url = url;
    this.date = date;
    this.cityId = cityId;
  }

  public EventDTO() {}

  public EventDTO(Event event) {
    this.id = event.getId();
    this.name = event.getName();
    this.url = event.getUrl();
    this.date = event.getDate();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Long getCityId() {
    return cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }
}
