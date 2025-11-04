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

import br.com.fiap.cinefinder.controller.GenreController;
import br.com.fiap.cinefinder.dto.GenreDto;
import br.com.fiap.cinefinder.dto.GetGenreDto;
import br.com.fiap.cinefinder.model.Genre;
import br.com.fiap.cinefinder.repository.GenreRepo;
import br.com.fiap.cinefinder.repository.MovieRepo;

@Service
public class GenreService {

    private final GenreRepo repo;
    private final MovieRepo movieRepo;

    public GenreService(GenreRepo repo, MovieRepo movieRepo) {
        this.repo = repo;
        this.movieRepo = movieRepo;
    }

    public Page<EntityModel<GetGenreDto>> getAll(Specification<Genre> specs, Pageable pageable) {
        return repo.findAll(specs, pageable).map(GenreService::toModel);
    }

    public EntityModel<GetGenreDto> getById(Long id) {
        return toModel(findByIdOrThrow(id));
    }

    public EntityModel<GetGenreDto> update(Long id, GenreDto upd) {
        var existing = findByIdOrThrow(id);
        existing.setName(upd.name() != null ? upd.name() : existing.getName());

        if (upd.moviesIds() != null) {
            movieRepo.findAllById(List.of(upd.moviesIds())).forEach(m -> m.addGenre(existing));
        }

        return toModel(repo.save(existing));
    }

    public EntityModel<GetGenreDto> save(GenreDto nGenre) {
        var genre = Genre.builder()
                .name(nGenre.name())
                .build();

        if (nGenre.moviesIds() != null) {
            movieRepo.findAllById(List.of(nGenre.moviesIds())).forEach(m -> m.addGenre(genre));
        }

        return toModel(repo.save(genre));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Genre findByIdOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Genre not found"));
    }

    private static EntityModel<GetGenreDto> toModel(Genre g) {
        var resource = EntityModel.of(GetGenreDto.fromGenre(g));
        resource.add(
                linkTo(methodOn(GenreController.class).getGenreById(g.getId())).withSelfRel(),
                linkTo(methodOn(GenreController.class).getAllGenres(null, Pageable.unpaged())).withRel("all-genres"));
        return resource;
    }

    public List<Genre> findAllByIds(Long[] genresIds) {
        return repo.findAllById(List.of(genresIds));
    }

}
