package br.com.fiap.cinefinder.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

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
import br.com.fiap.cinefinder.repository.AppUserRepo;
import br.com.fiap.cinefinder.repository.MovieRepo;
import br.com.fiap.cinefinder.repository.ReviewRepo;

@Service
public class ReviewService {

    private final ReviewRepo repo;
    private final MovieRepo movieRepo;
    private final AppUserRepo userRepo;

    public ReviewService(ReviewRepo repo, MovieRepo movieRepo, AppUserRepo userRepo) {
        this.repo = repo;
        this.movieRepo = movieRepo;
        this.userRepo = userRepo;
    }

    public Page<EntityModel<GetReviewDto>> getAll(Specification<Review> specs, Pageable pageable) {
        return repo.findAll(specs, pageable).map(ReviewService::toModel);
    }

    public EntityModel<GetReviewDto> getById(Long id) {
        return toModel(findByIdOrThrow(id));
    }

    public EntityModel<GetReviewDto> update(Long id,  ReviewDto upd) {

        var existing = findByIdOrThrow(id);
        existing.setTitle(upd.title() != null ? upd.title() : existing.getTitle());
        existing.setComments(upd.comments() != null ? upd.comments() : existing.getComments());
        existing.setRate(upd.rate() != null ? upd.rate() : existing.getRate());
        existing.setLocalization(upd.localization() != null ? upd.localization() : existing.getLocalization());

        return toModel(repo.save(existing));
    }

    public EntityModel<GetReviewDto> save( ReviewDto nReview) {
        if (userRepo.existsById(nReview.authorId()) || movieRepo.existsById(nReview.movieId())) {

            var review = new Review();
            review.setTitle(nReview.title());
            review.setComments(nReview.comments());
            review.setRate(nReview.rate());
            review.setLocalization(nReview.localization());
            review.associateToAuthor(userRepo.findById(nReview.authorId()).get());
            review.associateToMovie(movieRepo.findById(nReview.movieId()).get());
            return toModel(repo.save(review));
        }

        throw new ResponseStatusException(NOT_FOUND, "Usuário e/ou filme não encontrado(s)");
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

    public List<Review> findAllByIds(Long[] reviewsIds) {
        return repo.findAllById(List.of(reviewsIds));
    }

}
