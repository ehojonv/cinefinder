package br.com.fiap.cinefinder.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.cinefinder.controller.MovieController;
import br.com.fiap.cinefinder.dto.GetMovieDto;
import br.com.fiap.cinefinder.model.Movie;
import br.com.fiap.cinefinder.repository.MovieRepo;

@Service
public class MovieService {

    private final MovieRepo repo;

    public MovieService(MovieRepo repo) {
        this.repo = repo;
    }

    public Page<EntityModel<GetMovieDto>> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(MovieService::toModel);
    }

    public EntityModel<GetMovieDto> getById(Long id) {
        return toModel(findByIdOrThrow(id));
    }

    public EntityModel<GetMovieDto> update(Long id, Movie upd) {
        getById(id);
        upd.setId(id);
        return save(upd);
    }

    public EntityModel<GetMovieDto> save(Movie movie) {
        return toModel(repo.save(movie));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Movie findByIdOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Movie not found"));
    }

    private static EntityModel<GetMovieDto> toModel(Movie m) {
        var resource = EntityModel.of(GetMovieDto.fromMovie(m));
        resource.add(
                linkTo(methodOn(MovieController.class).getMovieById(m.getId())).withSelfRel(),
                linkTo(methodOn(MovieController.class).getAllMovies(Pageable.unpaged())).withRel("all-movies")
        );
        return resource;
    }

}
