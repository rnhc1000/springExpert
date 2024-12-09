package com.devsuperior.demo.services;


import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.repositories.CityRepository;
import com.devsuperior.demo.services.exceptions.ConstraintViolationException;
import com.devsuperior.demo.utils.DtoToEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityService {

  public CityService() {
  }

  public CityService(CityRepository cityRepository, DtoToEntity dtoToEntity) {
    this.cityRepository = cityRepository;
    this.dtoToEntity = dtoToEntity;
  }

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private DtoToEntity dtoToEntity;

  @Transactional
  public Page<CityDTO> findAllPaged(Pageable pageable) {

    Page<City> cities = cityRepository.findAllCustomPaged(pageable);

    return cities.map(CityDTO::new);
  }

  @Transactional
  public CityDTO insert(CityDTO cityDTO) {
    City city = new City();
    if (cityDTO.getName().isEmpty() || cityDTO.getName().isBlank()) {
      throw new ConstraintViolationException("City name can not be null or blank");
    }
    dtoToEntity.copyDtoToEntity(cityDTO, city);
    cityRepository.save(city);

    return new CityDTO(city);
  }

  public Optional<City> validateCityId(Long id) {

    return Optional.ofNullable(cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not registered!")));
  }

}
