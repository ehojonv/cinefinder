package br.com.fiap.cinefinder.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.cinefinder.controller.ReviewController;
import br.com.fiap.cinefinder.dto.GetReviewDto;
import br.com.fiap.cinefinder.model.Movie;
import br.com.fiap.cinefinder.model.Review;
import br.com.fiap.cinefinder.repository.MovieRepo;
import br.com.fiap.cinefinder.repository.ReviewRepo;

@Service
public class ReviewService {

    private final ReviewRepo repo;
    private final MovieRepo movieRepo;

    public ReviewService(ReviewRepo repo, MovieRepo movieRepo) {
        this.repo = repo;
        this.movieRepo = movieRepo;
    }

    public Page<EntityModel<GetReviewDto>> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(ReviewService::toModel);
    }

    public EntityModel<GetReviewDto> getById(Long id) {
        return toModel(findByIdOrThrow(id));
    }

    public EntityModel<GetReviewDto> update(Long id, Review upd) {
        getById(id);
        upd.setId(id);
        return save(upd);
    }

    public EntityModel<GetReviewDto> save(Review review) {
        var saved = repo.save(review);
        Movie movie = saved.getMovie();
        if (movie != null) {
            movie.calculateRating();
            movieRepo.save(movie);
        }
        return toModel(saved);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    private Review findByIdOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Review not found"));
    }

    private static EntityModel<GetReviewDto> toModel(Review r) {
        var resource = EntityModel.of(GetReviewDto.fromReview(r));
        resource.add(
                linkTo(methodOn(ReviewController.class).getReviewById(r.getId())).withSelfRel(),
                linkTo(methodOn(ReviewController.class).getAllReviews(Pageable.unpaged())).withRel("all-reviews")
        );
        return resource;
    }

}
