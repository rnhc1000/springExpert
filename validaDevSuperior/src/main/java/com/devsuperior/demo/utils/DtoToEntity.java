package com.devsuperior.demo.utils;

import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DtoToEntity {

  public DtoToEntity() {}

  public void copyDtoToEntity(CityDTO dto, City entity) {

    entity.setName(dto.getName());
  }

  public void copyDtoToEntity(EventDTO dto, Event entity) {

    entity.setName(dto.getName());
    entity.setDate(dto.getDate());
    entity.setUrl(dto.getUrl());
    entity.setCity(new Event(dto.getCityId()).getCity());

  }
}
