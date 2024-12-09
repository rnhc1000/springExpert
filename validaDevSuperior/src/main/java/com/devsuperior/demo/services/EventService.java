package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.EventCityDTO;
import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repositories.EventRepository;
import com.devsuperior.demo.services.exceptions.ConstraintViolationException;
import com.devsuperior.demo.utils.DtoToEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static java.util.Objects.isNull;

@Service
public class EventService {

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private CityService cityService;

  @Autowired
  private DtoToEntity dtoToEntity;

  public Page<EventCityDTO> getEventsByCity(Pageable pageable) {

    Page<Event> events = eventRepository.searchCityAndEvents(pageable);

    System.out.println(events);
    return events.map(event -> new EventCityDTO(
        event.getCityName(),
        event.getName(),
        event.getUrl(),
        event.getDate()));
  }

  public EventDTO registerEvent(EventDTO eventDTO) {

    if (eventDTO.getName().isEmpty()) {
      throw new ConstraintViolationException("Event can not be empty");
    }
    if (eventDTO.getCityId() == null) {
      throw new ConstraintViolationException("City can not be null");
    }

    LocalDate today = LocalDate.now();
    LocalDate eventDate = eventDTO.getDate();

    if (eventDate.isBefore(today)) {
      throw new ConstraintViolationException("No events in the past");
    }

    if (isNull(eventDTO.getCityId())) {
      throw new ConstraintViolationException("Define a City for an Event");
    }
    Long cityId = eventDTO.getCityId();



    if (cityService.validateCityId(eventDTO.getCityId()).isPresent()) {
      eventRepository.saveEvent(eventDTO.getName(), eventDTO.getDate(), eventDTO.getUrl(), eventDTO.getCityId());
    }
    return new EventDTO(eventDTO.getId(), eventDTO.getName(), eventDTO.getUrl(), eventDTO.getDate(), eventDTO.getCityId());
  }
}
