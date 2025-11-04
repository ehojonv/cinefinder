package br.com.fiap.cinefinder.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.cinefinder.controller.FlowController;
import br.com.fiap.cinefinder.dto.FlowDto;
import br.com.fiap.cinefinder.model.Flow;
import br.com.fiap.cinefinder.repository.AppUserRepo;
import br.com.fiap.cinefinder.repository.FlowRepo;
import br.com.fiap.cinefinder.repository.MovieRepo;

@Service
public class FlowService {

    private final FlowRepo repo;
    private final AppUserRepo userRepo;
    private final MovieRepo movieRepo;

    public FlowService(FlowRepo repo, AppUserRepo userRepo, MovieRepo movieRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.movieRepo = movieRepo;
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
        existing.setMovies(
                upd.movieIds() != null ? movieRepo.findAllById(List.of(upd.movieIds())) : existing.getMovies());
        return toModel(repo.save(existing));
    }

    public EntityModel<Flow> save(FlowDto nFlow) {

        if (userRepo.existsById(nFlow.authorId())) {

            var flow = Flow.builder()
                    .title(nFlow.title())
                    .author(userRepo.findById(nFlow.authorId()).get())
                    .movies(nFlow.movieIds() != null ? movieRepo.findAllById(List.of(nFlow.movieIds())) : List.of())
                    .build();
            return toModel(repo.save(flow));
        }

        throw new ResponseStatusException(NOT_FOUND, "Usuário não encontrado");
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
