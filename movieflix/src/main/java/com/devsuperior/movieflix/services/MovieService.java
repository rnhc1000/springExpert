package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

  private final MovieRepository movieRepository;



  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public Page<MovieCardDTO> getAllMoviesPaged(Pageable pageable) {

    Page<Movie> movies = movieRepository.findAllCustomPaged(pageable);

    return movies.map(movie ->new MovieCardDTO(
        movie.getId(),
        movie.getTitle(),
        movie.getSubTitle(),
        movie.getYear(),
        movie.getImgUrl()
    ));

//    return movies.map(MovieCardDTO::new);
  }

  public MovieDetailsDTO getMovieByd(Long id) {

    Optional<Movie> entity = movieRepository.findById(id);
    Movie movie = entity.orElseThrow(() -> new ResourceNotFoundException("Movie not Found!"));

    return new MovieDetailsDTO(
        movie.getId(),
        movie.getTitle(),
        movie.getSubTitle(),
        movie.getYear(),
        movie.getImgUrl(),
        movie.getSynopsis(),
        new GenreDTO(movie.getGenre().getId(), movie.getGenre().getName())
    );
  }

  public List<MovieDetailsDTO> getMovieByGenreId(Long genreId) {
    List<Optional<Movie>> movies = movieRepository.findMovieByGenreId(genreId);
    List<MovieDetailsDTO> moviesByGenre = new ArrayList<>();

    if (!movies.isEmpty()) {

      for (Optional<Movie> movie : movies) {

        movie.ifPresent(value -> moviesByGenre.add(new MovieDetailsDTO(
            value.getId(), value.getTitle(), value.getSubTitle(), value.getYear(),
            value.getImgUrl(), value.getSynopsis(),
            new GenreDTO(value.getGenre().getId(), value.getGenre().getName()))));
      }

      return moviesByGenre;
    } else {
      throw new ResourceNotFoundException("Movie not Found!");
    }

  }

  public Page<MovieDetailsDTO> getMovieByGenreIdPaged(Pageable pageable, Long genreId) {
    Page<Movie> movies = movieRepository.findMovieByGenreIdPaged(pageable, genreId);
    List<MovieDetailsDTO> moviesByGenre = new ArrayList<>();

    if (!movies.isEmpty()) {

      for (Movie movie : movies) {

        moviesByGenre.add(new MovieDetailsDTO(
            movie.getId(), movie.getTitle(), movie.getSubTitle(), movie.getYear(),
            movie.getImgUrl(), movie.getSynopsis(),
            new GenreDTO(movie.getGenre().getId(), movie.getGenre().getName())));
      }

      return new PageImpl<>(moviesByGenre, pageable, moviesByGenre.size());
    } else {
      throw new ResourceNotFoundException("Movie not Found!");
    }

  }
}
