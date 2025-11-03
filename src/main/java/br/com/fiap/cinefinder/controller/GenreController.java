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

import br.com.fiap.cinefinder.dto.GenreDto;
import br.com.fiap.cinefinder.model.Genre;
import br.com.fiap.cinefinder.service.GenreService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("genres")
@Slf4j
public class GenreController {

    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping
    public Page<EntityModel<Genre>> getAllGenres(@PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
        log.info("recuperando todos os genres");
        return service.getAll(pageable);
    }

    @GetMapping("{id}")
    public EntityModel<Genre> getGenreById(@PathVariable Long id) {
        log.info("recuperando genre pelo id: {}", id);
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = CREATED)
    public EntityModel<Genre> createGenre(@RequestBody GenreDto genre) {
        log.info("criando novo genre: {}", genre);
        return service.save(genre);
    }

    @PutMapping("{id}")
    public EntityModel<Genre> updateGenre(@PathVariable Long id, @RequestBody GenreDto upd) {
        log.info("atualizando genre id: {} com os dados: {}", id, upd);
        return service.update(id, upd);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteGenre(@PathVariable Long id) {
        log.info("deletando genre pelo id: {}", id);
        service.delete(id);
    }

}
