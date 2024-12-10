package com.devsuperior.demo.service;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repository.EventRepository;
import com.devsuperior.demo.service.exceptions.IllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EventService {

  @Autowired
  private EventRepository eventRepository;

  public EventService() {
  }

  public EventDTO updateEvent(Long id, EventDTO eventDTO) {

    Event event = new Event();
    Optional<Event> existingEvent = eventRepository.findById(id);

    if(existingEvent.isPresent()) {

      eventRepository.saveEvent(eventDTO.getName(), eventDTO.getDate(),
          eventDTO.getUrl(), eventDTO.getCityId());
    } else {
      throw new IllegalArgumentException("Invalid Event");
    }

    eventDTO.setId(id);

    return eventDTO;
  }
}
