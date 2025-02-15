package com.devsuperior.dsmovie.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.services.ScoreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/scores")
public class ScoreController {

	public ScoreController(ScoreService service) {
		this.service = service;
	}

	private final ScoreService service;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
	@PutMapping
	public ResponseEntity<MovieDTO> saveScore(@Valid @RequestBody ScoreDTO dto) {
		MovieDTO movieDTO = service.saveScore(dto);
		return ResponseEntity.ok().body(movieDTO);
	}
}
