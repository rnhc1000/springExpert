package com.devsuperior.demo.resources;


import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.services.CityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class CityResource {

  private static final Logger logger = LoggerFactory.getLogger(CityResource.class);
  @Autowired
  private CityService cityService;

  @GetMapping(value = "/cities")
  public ResponseEntity<List<CityDTO>> getAllCities() {
    List<CityDTO> cities = cityService.searchAllCities();

    return ResponseEntity.ok().body(cities);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping(value = "/cities")
  public ResponseEntity<CityDTO> insert(@Valid @RequestBody CityDTO cityDTO) {
    cityDTO = cityService.insert(cityDTO);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(cityDTO.getId()).toUri();

    logger.info("CityDTO -> {}", cityDTO);
    return ResponseEntity.created(uri).body(cityDTO);
  }
}
