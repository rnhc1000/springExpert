package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {

  @InjectMocks
  private MovieService movieService;

  private MovieDTO movieDTO;

  private MovieEntity movie;

  private String movieName;

  private PageImpl<MovieEntity> page;
  private Long existingMovieId, nonExistingMovieId, dependentMovieId;

  @Mock
  private MovieRepository movieRepository;


  @BeforeEach
  void setup() throws Exception {

    movie = MovieFactory.createMovieEntity();
    movieDTO = new MovieDTO(movie);
    movieName = "Test Movie";
    page = new PageImpl<>(List.of(movie));
    existingMovieId = 1L;
    nonExistingMovieId = 100L;
    dependentMovieId = 2L;


    Mockito.when(movieRepository.searchByTitle(any(), any())).thenReturn(page);
    Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));

    Mockito.when(movieRepository.getReferenceById(existingMovieId)).thenReturn(movie);
    Mockito.when(movieRepository.getReferenceById(nonExistingMovieId)).thenThrow(EntityNotFoundException.class);

    Mockito.when(movieRepository.save(any())).thenReturn(movie);

    Mockito.when(movieRepository.existsById(existingMovieId)).thenReturn(true);
    Mockito.when(movieRepository.existsById(nonExistingMovieId)).thenReturn(false);
    Mockito.when(movieRepository.existsById(dependentMovieId)).thenReturn(true);

    Mockito.doNothing().when(movieRepository).deleteById(existingMovieId);
    Mockito.doThrow(DataIntegrityViolationException.class).when(movieRepository).deleteById(dependentMovieId);

  }

  @Test
  void findAllShouldReturnPagedMovieDTO() {

    Pageable pageable = PageRequest.of(0, 20);
    Page<MovieDTO> result = movieService.findAll(movieName, pageable);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.getSize());
    Assertions.assertEquals(result.iterator().next().getTitle(), movieName);

  }

  @Test
  void findByIdShouldReturnMovieDTOWhenIdExists() {

    MovieDTO result = movieService.findById(existingMovieId);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getId(), existingMovieId);

  }

  @Test
  void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      movieService.findById(nonExistingMovieId);
    });

  }

  @Test
  void insertShouldReturnMovieDTO() {

    MovieDTO result = movieService.insert(movieDTO);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(movieName, result.getTitle());

  }

  @Test
  void updateShouldReturnMovieDTOWhenIdExists() {

    MovieDTO result = movieService.update(existingMovieId, movieDTO);

    Assertions.assertNotNull(result);

  }

  @Test
  void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      movieService.update(nonExistingMovieId, movieDTO);
    });

  }

  @Test
  void deleteShouldDoNothingWhenIdExists() {

    Assertions.assertDoesNotThrow(() -> {
      movieService.delete(existingMovieId);
    });

  }

  @Test
  void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      movieService.delete(nonExistingMovieId);
    });

  }

  @Test
  void deleteShouldThrowDatabaseExceptionWhenDependentId() {

    Assertions.assertThrows(DatabaseException.class, () -> {
      movieService.delete(dependentMovieId);
    });

  }
}
