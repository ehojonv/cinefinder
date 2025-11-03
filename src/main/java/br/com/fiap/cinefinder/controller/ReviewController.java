package br.com.fiap.cinefinder.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.cinefinder.dto.GetReviewDto;
import br.com.fiap.cinefinder.dto.ReviewDto;
import br.com.fiap.cinefinder.filters.ReviewFilter;
import br.com.fiap.cinefinder.filters.Specifications;
import br.com.fiap.cinefinder.service.ReviewService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("reviews")
@Slf4j
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping
    public Page<EntityModel<GetReviewDto>> getAllReviews(ReviewFilter filter,
            @PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
        log.info("recuperando todos os reviews");
        var specs = Specifications.buildReview(filter);
        return service.getAll(specs, pageable);
    }

    @GetMapping("{id}")
    public EntityModel<GetReviewDto> getReviewById(@PathVariable Long id) {
        log.info("recuperando review pelo id: {}", id);
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = CREATED)
    public EntityModel<GetReviewDto> createReview(@RequestBody ReviewDto review) {
        log.info("criando novo review: {}", review);
        return service.save(review);
    }

    @PutMapping("{id}")
    public EntityModel<GetReviewDto> updateReview(@PathVariable Long id, @RequestBody ReviewDto upd) {
        log.info("atualizando review id: {} com os dados: {}", id, upd);
        return service.update(id, upd);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id) {
        log.info("deletando review pelo id: {}", id);
        service.delete(id);
    }

}
