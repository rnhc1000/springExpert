package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.EventCityDTO;
import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repositories.CityRepository;
import com.devsuperior.demo.repositories.EventRepository;
import com.devsuperior.demo.services.exceptions.ConstraintViolationException;
import com.devsuperior.demo.utils.DtoToEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static java.util.Objects.isNull;

@Service
public class EventService {

  public EventService(EventRepository eventRepository, CityRepository cityRepository, CityService cityService, DtoToEntity dtoToEntity) {
    this.eventRepository = eventRepository;
    this.cityRepository = cityRepository;
    this.cityService = cityService;
    this.dtoToEntity = dtoToEntity;
  }

  private final EventRepository eventRepository;

  private final CityRepository cityRepository;

  private final CityService cityService;

  private final DtoToEntity dtoToEntity;

  public Page<EventCityDTO> getEventsByCity(Pageable pageable) {

    Page<Event> events = eventRepository.searchCityAndEvents(pageable);

    return events.map(event -> new EventCityDTO(
        event.getCityName(),
        event.getName(),
        event.getUrl(),
        event.getDate()));
  }

  @Transactional
  public EventDTO registerEvent(EventDTO eventDTO) {

//    if (eventDTO.getName().isEmpty()) {
//      throw new ConstraintViolationException("Event can not be empty");
//    }
//    if (eventDTO.getCityId() == null) {
//      throw new ConstraintViolationException("City can not be null");
//    }
//
//    LocalDate today = LocalDate.now();
//    LocalDate eventDate = eventDTO.getDate();
//
//    if (eventDate.isBefore(today)) {
//      throw new ConstraintViolationException("No events in the past");
//    }
//
//    if (isNull(eventDTO.getCityId())) {
//      throw new ConstraintViolationException("Define a City for an Event");
//    }


    if (cityService.validateCityId(eventDTO.getCityId()).isPresent()) {

      Event event =  new Event();
      City city = new City();
      city.setId(eventDTO.getCityId());
      event.setId(null);
      event.setName(eventDTO.getName());
      event.setDate(eventDTO.getDate());
      event.setUrl(eventDTO.getUrl());
      event.setCity(city);
//      event.setCity(cityRepository.getReferenceById(eventDTO.getCityId()));
      event = eventRepository.save(event);
      System.out.println(event);

//          eventRepository.saveEvent(eventDTO.getName(), eventDTO.getDate(), eventDTO.getUrl(), eventDTO.getCity_id());
      return new EventDTO(event);
    } else {
      throw new ConstraintViolationException("City is not registered");
    }

    }
  }
