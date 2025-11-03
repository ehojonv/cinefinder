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

import br.com.fiap.cinefinder.controller.MovieController;
import br.com.fiap.cinefinder.dto.GetMovieDto;
import br.com.fiap.cinefinder.dto.MovieDto;
import br.com.fiap.cinefinder.model.Movie;
import br.com.fiap.cinefinder.repository.MovieRepo;

@Service
public class MovieService {

    private final MovieRepo repo;
    private final ReviewService reviewService;
    private final GenreService genreService;

    public MovieService(MovieRepo repo, ReviewService reviewService, GenreService genreService) {
        this.repo = repo;
        this.reviewService = reviewService;
        this.genreService = genreService;
    }

    public Page<EntityModel<GetMovieDto>> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(MovieService::toModel);
    }

    public EntityModel<GetMovieDto> getById(Long id) {
        return toModel(findByIdOrThrow(id));
    }

    public EntityModel<GetMovieDto> update(Long id, MovieDto upd) {
        var existing = findByIdOrThrow(id);
        existing.setTitle(upd.title() != null ? upd.title() : existing.getTitle());
        existing.setSynopsis(upd.synopsis() != null ? upd.synopsis() : existing.getSynopsis());
        existing.setGenres(upd.genresIds() != null ? genreService.findAllByIds(upd.genresIds()) : existing.getGenres());
        return save(upd);
    }

    public EntityModel<GetMovieDto> save(MovieDto movieToSave) {
        var movie = Movie.builder()
                .title(movieToSave.title())
                .synopsis(movieToSave.synopsis())
                .releaseDate(movieToSave.releaseDate())
                .genres(genreService.findAllByIds(movieToSave.genresIds()))
                .build();
        reviewService.findAllByIds(movieToSave.reviewsIds()).forEach(r -> r.associateToMovie(movie));
        movie.calculateRating();
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
                linkTo(methodOn(MovieController.class).getAllMovies(Pageable.unpaged())).withRel("all-movies"));
        return resource;
    }

    public List<Movie> findAllByIds(Long[] movieIds) {
        return repo.findAllById(List.of(movieIds));
    }

}
