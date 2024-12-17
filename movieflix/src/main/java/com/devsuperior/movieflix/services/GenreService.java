package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

  @Autowired
  private GenreRepository genreRepository;


  public List<GenreDTO> retrieveGenres() {

    List<Genre> genres = genreRepository.findAll();

    return genres.stream().map(genre -> new GenreDTO(genre.getId(), genre.getName())).toList();
  }
}
