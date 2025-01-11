package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.MovieGenreDTO;
import com.devsuperior.dsmovie.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/v1/movies")
public class MovieControllerV1 {

	private final MovieService service;

  public MovieControllerV1(MovieService service) {
    this.service = service;
  }

  @GetMapping
	public Page<MovieGenreDTO> findAll(
			@RequestParam(value="title", defaultValue = "") String title,
			Pageable pageable) {
		return service.findAllMovieGenre(title, pageable);
	}

	@GetMapping(value = "/{id}")
	public MovieGenreDTO findById(@PathVariable Long id) {
		return service.findByIdMovieGenre(id);
	}


}
