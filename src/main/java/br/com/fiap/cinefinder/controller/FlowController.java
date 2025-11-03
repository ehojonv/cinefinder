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

import br.com.fiap.cinefinder.dto.FlowDto;
import br.com.fiap.cinefinder.model.Flow;
import br.com.fiap.cinefinder.service.FlowService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("flows")
@Slf4j
public class FlowController {

    private final FlowService service;

    public FlowController(FlowService service) {
        this.service = service;
    }

    @GetMapping
    public Page<EntityModel<Flow>> getAllFlows(@PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
        log.info("recuperando todos os flows");
        return service.getAll(pageable);
    }

    @GetMapping("{id}")
    public EntityModel<Flow> getFlowById(@PathVariable Long id) {
        log.info("recuperando flow pelo id: {}", id);
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = CREATED)
    public EntityModel<Flow> createFlow(@RequestBody FlowDto flow) {
        log.info("criando novo flow: {}", flow);
        return service.save(flow);
    }

    @PutMapping("{id}")
    public EntityModel<Flow> updateFlow(@PathVariable Long id, @RequestBody FlowDto upd) {
        log.info("atualizando flow id: {} com os dados: {}", id, upd);
        return service.update(id, upd);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteFlow(@PathVariable Long id) {
        log.info("deletando flow pelo id: {}", id);
        service.delete(id);
    }

}
