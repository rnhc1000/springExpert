package com.devsuperior.demo.repository;

import com.devsuperior.demo.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  @Transactional
  @Modifying
  @Query(nativeQuery = true, value =
      """
          INSERT INTO tb_event (name, date, url, city_id) VALUES (:name, :date, :url, :city_id)
      """
  )
  void saveEvent(@Param("name") String name, @Param("date") LocalDate date,
                 @Param("url") String url, @Param("city_id") Long city_id);

  @Query(nativeQuery = true, value =
      """
          SELECT id, name, date, url, city_id FROM tb_event t WHERE city_id = :city_id LIMIT 1
      """
  )
  Optional<Event> findEventByCityId(Long city_id);
}
