package com.devsuperior.demo.repositories;

import com.devsuperior.demo.entities.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

  @Query(nativeQuery = true, value =
      """
          SELECT id i, name n FROM tb_city c ORDER BY name ASC
      """
  )
  Page<City> findAllCustomPaged(Pageable pageable);
}
