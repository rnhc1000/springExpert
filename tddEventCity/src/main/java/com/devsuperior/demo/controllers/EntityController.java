package com.devsuperior.demo.controllers;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.*;

@RestController
public class EntityController {

  @Autowired
  private EventService eventService;

  @PutMapping("/events/{id}")
  public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {

    eventDTO = eventService.updateEvent(id, eventDTO);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
        .buildAndExpand(eventDTO.getId()).toUri();

    return ResponseEntity.ok().body(eventDTO);
  }
}
