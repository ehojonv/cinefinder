package br.com.fiap.cinefinder.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.cinefinder.controller.GenreController;
import br.com.fiap.cinefinder.model.Genre;
import br.com.fiap.cinefinder.repository.GenreRepo;

@Service
public class GenreService {

    private final GenreRepo repo;

    public GenreService(GenreRepo repo) {
        this.repo = repo;
    }

    public Page<EntityModel<Genre>> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(GenreService::toModel);
    }

    public EntityModel<Genre> getById(Long id) {
        return toModel(findByIdOrThrow(id));
    }

    public EntityModel<Genre> update(Long id, Genre upd) {
        getById(id);
        upd.setId(id);
        return save(upd);
    }

    public EntityModel<Genre> save(Genre genre) {
        return toModel(repo.save(genre));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Genre findByIdOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Genre not found"));
    }

    private static EntityModel<Genre> toModel(Genre g) {
        var resource = EntityModel.of(g);
        resource.add(
                linkTo(methodOn(GenreController.class).getGenreById(g.getId())).withSelfRel(),
                linkTo(methodOn(GenreController.class).getAllGenres(Pageable.unpaged())).withRel("all-genres")
        );
        return resource;
    }

}
