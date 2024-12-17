package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.services.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  private final MovieService movieService;

  @PreAuthorize("hasAnyRole('ROLE_VISITOR', 'ROLE_MEMBER')")
  @GetMapping("/movies")
  public ResponseEntity<Page<MovieCardDTO>> searchMovies(Pageable pageable) {
    Page<MovieCardDTO> movies = movieService.getAllMoviesPaged(pageable);

    return ResponseEntity.ok().body(movies);
  }

  @PreAuthorize("hasAnyRole('ROLE_VISITOR', 'ROLE_MEMBER')")
  @GetMapping(value = "/movies/{id}")
  public ResponseEntity<MovieDetailsDTO> searchMoviesById(@PathVariable("id") Long id) {
  MovieDetailsDTO movie = movieService.getMovieByd(id);

    return ResponseEntity.ok().body(movie);
  }

  @PreAuthorize("hasAnyRole('ROLE_VISITOR', 'ROLE_MEMBER')")
  @GetMapping(value = "/movies", params = {"genreId"})
  public ResponseEntity<Page<MovieDetailsDTO>> searchMoviesByGenre(@RequestParam(required = false, defaultValue = "1") Long genreId, Pageable pageable) {
    Page<MovieDetailsDTO> movies = movieService.getMovieByGenreIdPaged(pageable, genreId);

    return ResponseEntity.ok().body(movies);
  }

}
