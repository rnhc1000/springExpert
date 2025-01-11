package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.controllers.MovieController;
import com.devsuperior.dsmovie.dto.MovieGenreDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MovieService {

	private static final String RESOURCE_NOT_FOUND = "Resource not found!!!";
	private final MovieRepository repository;

  public MovieService(MovieRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
	public Page<MovieDTO> findAll(String title, Pageable pageable) {
		Page<MovieEntity> movies = repository.searchByTitle(title, pageable);
		return movies.map(movie -> new MovieDTO(movie)
				.add(linkTo(methodOn(MovieController.class).findAll(null, null))
						.withSelfRel())
						.add(linkTo(methodOn(MovieController.class).findById(movie.getId()))
								.withRel("Get movie by id"))
				);
	}

	@Transactional(readOnly = true)
	public MovieDTO findById(Long id) {
		MovieEntity result = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND));
		return new MovieDTO(result)
				.add(linkTo(methodOn(MovieController.class)
						.findById(result.getId()))
						.withSelfRel())
				.add(linkTo(methodOn(MovieController.class)
						.findAll(null, null))
						.withRel("All movies"))
				.add(linkTo(methodOn(MovieController.class)
						.update(id, null))
						.withRel("Update movie"))
				.add(linkTo(methodOn(MovieController.class)
						.delete(id))
						.withRel("Delete Movie!")
				);
	}

	@Transactional(readOnly = true)
	public Page<MovieGenreDTO> findAllMovieGenre(String title, Pageable pageable) {
		Page<MovieEntity> result = repository.searchByTitle(title, pageable);
		return result.map(MovieGenreDTO::new);
	}

	@Transactional(readOnly = true)
	public MovieGenreDTO findByIdMovieGenre(Long id) {
		MovieEntity result = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND));
		return new MovieGenreDTO(result)
				.add(linkTo(methodOn(MovieController.class)
						.findById(result.getId()))
						.withSelfRel())
				.add(linkTo(methodOn(MovieController.class)
						.findAll(null, null))
						.withRel("All movies"))
				.add(linkTo(methodOn(MovieController.class)
						.update(id, null))
						.withRel("Update movie"))
				.add(linkTo(methodOn(MovieController.class)
						.delete(id))
						.withRel("Delete Movie!")
				);
	}

	@Transactional
	public MovieDTO insert(MovieDTO dto) {
		MovieEntity entity = new MovieEntity();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new MovieDTO(entity);
	}

	@Transactional
	public MovieDTO update(Long id, MovieDTO dto) {
		try {
			MovieEntity entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new MovieDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
		}
	}

	public void delete(Long id) {
		if (!repository.existsById(id))
			throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

	private void copyDtoToEntity(MovieDTO dto, MovieEntity entity) {
		entity.setTitle(dto.getTitle());
		entity.setScore(dto.getScore());
		entity.setCount(dto.getCount());
		entity.setImage(dto.getImage());
	}
}