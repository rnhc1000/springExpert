package com.devsuperior.demo.repositories;

import com.devsuperior.demo.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

@Query(nativeQuery = true, value =
    """
        SELECT c.name AS city, t.name,
        t.url AS url, t.date AS date,
        t.city_id, t.id
        FROM tb_event t
        INNER JOIN tb_city c WHERE t.city_id = c.id ORDER BY date ASC
    """
)
Page<Event> searchCityAndEvents(Pageable pageable);

@Transactional
@Modifying
@Query(nativeQuery = true, value =
    """
        INSERT INTO tb_event (name, date, url, city_id) VALUES (:name, :date, :url, :city_id)
    """
)
void saveEvent(@Param("name") String name, @Param("date") LocalDate date,
               @Param("url") String url, @Param("city_id") Long city_id);
}
