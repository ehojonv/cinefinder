package br.com.fiap.cinefinder.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.cinefinder.controller.ReviewController;
import br.com.fiap.cinefinder.dto.GetReviewDto;
import br.com.fiap.cinefinder.dto.ReviewDto;
import br.com.fiap.cinefinder.model.Review;
import br.com.fiap.cinefinder.repository.ReviewRepo;

@Service
public class ReviewService {

    private final ReviewRepo repo;
    private final MovieService movieService;
    private final AppUserService userService;

    public ReviewService(ReviewRepo repo, MovieService movieService, AppUserService userService) {
        this.repo = repo;
        this.movieService = movieService;
        this.userService = userService;
    }

    public Page<EntityModel<GetReviewDto>> getAll(Specification<Review> specs, Pageable pageable) {
        return repo.findAll(specs, pageable).map(ReviewService::toModel);
    }

    public EntityModel<GetReviewDto> getById(Long id) {
        return toModel(findByIdOrThrow(id));
    }

    public EntityModel<GetReviewDto> update(Long id, ReviewDto upd) {

        var existing = findByIdOrThrow(id);
        existing.setTitle(upd.title() != null ? upd.title() : existing.getTitle());
        existing.setComments(upd.comments() != null ? upd.comments() : existing.getComments());
        existing.setRate(upd.rate() != null ? upd.rate() : existing.getRate());
        existing.setLocalization(upd.localization() != null ? upd.localization() : existing.getLocalization());
        existing.setAuthor(userService.findByIdOrThrow(id));
        existing.setMovie(movieService.findByIdOrThrow(id));
        
        return toModel(repo.save(existing));
    }

    public EntityModel<GetReviewDto> save(ReviewDto pstReview) {
        var review = new Review();
        review.setTitle(pstReview.title());
        review.setComments(pstReview.comments());
        review.setRate(pstReview.rate());
        review.setLocalization(pstReview.localization());
        review.associateToAuthor(userService.findByIdOrThrow(pstReview.authorId()));
        review.associateToMovie(movieService.findByIdOrThrow(pstReview.movieId()));
        return toModel(repo.save(review));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Review findByIdOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Review not found"));
    }

    private static EntityModel<GetReviewDto> toModel(Review r) {
        var resource = EntityModel.of(GetReviewDto.fromReview(r));
        resource.add(
                linkTo(methodOn(ReviewController.class).getReviewById(r.getId())).withSelfRel(),
                linkTo(methodOn(ReviewController.class).getAllReviews(null, Pageable.unpaged()))
                        .withRel("all-reviews"));
        return resource;
    }

}
