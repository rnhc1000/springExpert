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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventService {

  public EventService(EventRepository eventRepository, CityService cityService) {
    this.eventRepository = eventRepository;
    this.cityService = cityService;
  }

  private final EventRepository eventRepository;

  private final CityService cityService;

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
    if (cityService.validateCityId(eventDTO.getCityId()).isPresent()) {

      Event event = new Event();
      City city = new City();
      city.setId(eventDTO.getCityId());
      event.setId(null);
      event.setName(eventDTO.getName());
      event.setDate(eventDTO.getDate());
      event.setUrl(eventDTO.getUrl());
      event.setCity(city);
      event = eventRepository.saveAndFlush(event);

      Long idEvent = event.getId();
      eventDTO.setId(idEvent);

      return eventDTO;
    } else {
      throw new ConstraintViolationException("City is not registered");
    }

  }
}
