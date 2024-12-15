package com.devsuperior.demo.dto;

public class EventInsertDTO extends EventDTO {

  private Long cityId;

  public EventInsertDTO() {
    super();
  }

  @Override
  public Long getCityId() {
    return cityId;
  }

  @Override
  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }
}
