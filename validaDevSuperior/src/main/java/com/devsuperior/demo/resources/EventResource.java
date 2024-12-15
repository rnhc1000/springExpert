package com.devsuperior.demo.resources;

import com.devsuperior.demo.dto.EventCityDTO;
import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class EventResource {

  @Autowired
  private EventService eventService;

  @GetMapping(value = "/events")
  public ResponseEntity<Page<EventCityDTO>> getAllEvents(Pageable pageable) {

    Page<EventCityDTO> events = eventService.getEventsByCity(pageable);

    return ResponseEntity.ok().body(events);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
  @PostMapping(value = "/events")
  public ResponseEntity<EventDTO> insert(@Valid @RequestBody EventDTO eventDTO) {
    eventDTO = eventService.registerEvent(eventDTO);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(eventDTO.getId()).toUri();
    return ResponseEntity.created(uri).body(eventDTO);

  }
}
