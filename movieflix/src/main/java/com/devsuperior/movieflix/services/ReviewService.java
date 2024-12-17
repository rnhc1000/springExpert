package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

  private final ReviewRepository reviewRepository;

  public ReviewService(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }

  @Transactional
  public ReviewDTO insertReview(ReviewDTO reviewDTO) {

    Review review = new Review();
    Movie movie = new Movie();
    User user = new User();
    user.setId(reviewDTO.getUserId());
    user.setEmail(reviewDTO.getUserEmail());
    user.setName(reviewDTO.getUserName());
    movie.setId(reviewDTO.getMovieId());
    review.setMovie(movie);
    review.setUser(user);
    review.setText(reviewDTO.getText());

    review = reviewRepository.saveAndFlush(review);

    Long idUser = review.getUser().getId();
//    String nameUser = review.getUser().getUsername();
//    String mailUser = review.getUser().getEmail();
    reviewDTO.setUserId(idUser);

    return reviewDTO;
  }
}
