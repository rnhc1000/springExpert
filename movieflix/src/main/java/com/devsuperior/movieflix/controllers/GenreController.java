package com.devsuperior.movieflix.controllers;


import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.services.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GenreController {

  public GenreController(GenreService genreService) {
    this.genreService = genreService;
  }

  private final GenreService genreService;


  @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_VISITOR')")
  @GetMapping(value = "/genres")
  public ResponseEntity<List<GenreDTO>> searchGenres() {

    List<GenreDTO> genreDTO = genreService.retrieveGenres();

    return ResponseEntity.ok().body(genreDTO);
  }
}
