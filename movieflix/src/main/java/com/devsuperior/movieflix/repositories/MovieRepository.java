package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

  @Query(nativeQuery = true, value =
      """
          SELECT ID, TITLE, GENRE_ID, SUB_TITLE, MOVIE_YEAR, IMG_URL, SYNOPSIS FROM tb_movie ORDER BY TITLE ASC
      """
  )
  Page<Movie> findAllCustomPaged(Pageable pageable);

  @Query(nativeQuery = true, value =
      """
          SELECT t.id, t.title, t.genre_id, t.sub_title, t.movie_year, t.img_url, t.synopsis FROM tb_movie t WHERE t.genre_id = ?1 ORDER BY t.id ASC
      """
  )
  List<Optional<Movie>> findMovieByGenreId(Long genre_id);


  @Query(nativeQuery = true, value =
      """
          SELECT t.id, t.title, t.genre_id, t.sub_title, t.movie_year, t.img_url, t.synopsis FROM tb_movie t WHERE t.genre_id = ?1 ORDER BY t.id ASC
      """
  )
  Page<Movie> findMovieByGenreIdPaged(Pageable pageable, Long genre_id);
}
