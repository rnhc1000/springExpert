package com.devsuperior.demo.service;

import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repository.CityRepository;
import com.devsuperior.demo.repository.EventRepository;
import com.devsuperior.demo.service.exceptions.DataIntegrityViolationException;
import com.devsuperior.demo.service.exceptions.IllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private EventRepository eventRepository;

  @Transactional
  public Page<CityDTO> getAllCities(Pageable pageable) {

    Page<City> cities = cityRepository.findAllCustomPaged(pageable);

    return cities.map(city -> new CityDTO(city.getName()));
  }

  @Transactional
  public List<CityDTO> getCities() {

    List<City> cities = cityRepository.findAll(Sort.by("name"));
    List<CityDTO> list = new ArrayList<>();
    for (City city : cities) {
      list.add(new CityDTO(city.getId(), city.getName()));

    }
    return list;
  }

  public CityDTO insert(CityDTO cityDTO) {

    City city = new City();
    city.setId(null);
    city.setName(cityDTO.getName());
    city = cityRepository.save(city);

    return new CityDTO(city);
  }


  @Transactional
  public void deleteCity(Long id) {

    Optional<Event> event = eventRepository.findEventByCityId(id);
    if (event.isPresent()) {
      throw new DataIntegrityViolationException("City can not be deleted");
    } else {
      cityRepository.deleteById(id);
      cityRepository.flush();
    }


  }


  public Optional<City> getCityGivenId(Long id) {

    return Optional.ofNullable(cityRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id not found in the DB!")));
  }
}
