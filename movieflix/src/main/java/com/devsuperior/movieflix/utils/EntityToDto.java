package com.devsuperior.movieflix.utils;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Review;

public class EntityToDto {

  public void copyDtoToEntity(GenreDTO genreDTO, Genre entity) {

    entity.setName(genreDTO.getName());
  }

  public void copyDtoToEntity(ReviewDTO reviewDTO, Review entity) {

    entity.setText(reviewDTO.getText());
  }
}
