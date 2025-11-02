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
import br.com.fiap.cinefinder.dto.GetFlowDto;
import br.com.fiap.cinefinder.model.Flow;
import br.com.fiap.cinefinder.repository.FlowRepo;

@Service
public class FlowService {

    private final FlowRepo repo;

    public FlowService(FlowRepo repo) {
        this.repo = repo;
    }

    public Page<EntityModel<GetFlowDto>> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(FlowService::toModel);
    }

    public EntityModel<GetFlowDto> getById(Long id) {
        return toModel(findByIdOrThrow(id));
    }

    public EntityModel<GetFlowDto> update(Long id, Flow upd) {
        getById(id);
        upd.setId(id);
        return save(upd);
    }

    public EntityModel<GetFlowDto> save(Flow flow) {
        return toModel(repo.save(flow));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    private Flow findByIdOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Flow not found"));
    }

    private static EntityModel<GetFlowDto> toModel(Flow f) {
        var resource = EntityModel.of(GetFlowDto.fromFlow(f));
        resource.add(
                linkTo(methodOn(FlowController.class).getFlowById(f.getId())).withSelfRel(),
                linkTo(methodOn(FlowController.class).getAllFlows(Pageable.unpaged())).withRel("all-flows")
        );
        return resource;
    }

}
