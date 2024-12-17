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

  private final UserService userService;

  public ReviewService(ReviewRepository reviewRepository, UserService userService) {
    this.reviewRepository = reviewRepository;
    this.userService = userService;
  }

  @Transactional
  public ReviewDTO insertReview(ReviewDTO reviewDTO) {

    Review review = new Review();
    Movie movie = new Movie();
    User user = userService.getUserAuthenticated();

    reviewDTO.setUserEmail(user.getEmail());
    reviewDTO.setUserName(user.getName());
    reviewDTO.setUserId(user.getId());
    movie.setId(reviewDTO.getMovieId());
    review.setText(reviewDTO.getText());
    review.setMovie(movie);
    review.setUser(user);

    review = reviewRepository.save(review);
    Long idReview = review.getId();
    reviewDTO.setId(idReview);

    return reviewDTO;
  }
}
