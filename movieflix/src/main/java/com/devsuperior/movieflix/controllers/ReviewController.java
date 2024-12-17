package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class ReviewController {

  private final ReviewService reviewService;

  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PreAuthorize("hasRole('ROLE_MEMBER')")
  @PostMapping(value ="/reviews")
  public ResponseEntity<ReviewDTO> addReview(@Valid @RequestBody ReviewDTO reviewDTO) {
    reviewDTO = reviewService.insertReview(reviewDTO);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
        .path("/{id}")
        .buildAndExpand(reviewDTO.getId()).toUri();

    return ResponseEntity.created(uri).body(reviewDTO);
  }
}
