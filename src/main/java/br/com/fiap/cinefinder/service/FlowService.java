package br.com.fiap.cinefinder.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.cinefinder.controller.FlowController;
import br.com.fiap.cinefinder.dto.FlowDto;
import br.com.fiap.cinefinder.model.Flow;
import br.com.fiap.cinefinder.repository.FlowRepo;

@Service
public class FlowService {

    private final FlowRepo repo;
    private final AppUserService userService;
    private final MovieService movieService;

    public FlowService(FlowRepo repo, AppUserService userService, MovieService movieService) {
        this.repo = repo;
        this.userService = userService;
        this.movieService = movieService;
    }

    public Page<EntityModel<Flow>> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(FlowService::toModel);
    }

    public EntityModel<Flow> getById(Long id) {
        return toModel(findByIdOrThrow(id));
    }

    public EntityModel<Flow> update(Long id, FlowDto upd) {
        var existing = findByIdOrThrow(id);
        existing.setTitle(upd.title() != null ? upd.title() : existing.getTitle());
        existing.setMovies(movieService.findAllByIds(upd.movieIds()));
        return toModel(repo.save(existing));
    }

    public EntityModel<Flow> save(FlowDto saveFlow) {
        var flow = Flow.builder()
            .title(saveFlow.title())
            .author(userService.findByIdOrThrow(saveFlow.authorId()))
            .movies(movieService.findAllByIds(saveFlow.movieIds()))
            .build();
        return toModel(repo.save(flow));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Flow findByIdOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Flow not found"));
    }

    private static EntityModel<Flow> toModel(Flow f) {
        var resource = EntityModel.of(f);
        resource.add(
                linkTo(methodOn(FlowController.class).getFlowById(f.getId())).withSelfRel(),
                linkTo(methodOn(FlowController.class).getAllFlows(Pageable.unpaged())).withRel("all-flows"));
        return resource;
    }

}
