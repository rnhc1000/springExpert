package com.devsuperior.demo.controllers;

import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class CityController {

  @Autowired
  private CityService cityService;

//  @GetMapping(value = "/cities")
  public ResponseEntity<Page<CityDTO>> getCities(Pageable pageable) {

    return ResponseEntity.ok().body(cityService.getAllCities(pageable));
  }

  @GetMapping(value = "/cities")
  public ResponseEntity<List<CityDTO>> getCitiess() {

    return ResponseEntity.ok().body(cityService.getCities());
  }

  @PostMapping(value = "/cities")
  public ResponseEntity<CityDTO> insertCity(@RequestBody CityDTO cityDTO) {
    cityDTO = cityService.insert(cityDTO);
    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequestUri()
        .path("/{id}")
        .buildAndExpand((cityDTO.getId())).toUri();

    return ResponseEntity.created(uri).body(cityDTO);
  }

  @DeleteMapping(value = "cities/{id}")
  public ResponseEntity<Void> deleteCityById(@PathVariable  Long id) {

    Optional<City> city = cityService.getCityGivenId(id);
    cityService.deleteCity(city.get().getId());
    return ResponseEntity.noContent().build();
  }
}
