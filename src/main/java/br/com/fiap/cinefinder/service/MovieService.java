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
import br.com.fiap.cinefinder.repository.GenreRepo;
import br.com.fiap.cinefinder.repository.MovieRepo;
import br.com.fiap.cinefinder.repository.ReviewRepo;

@Service
public class MovieService {

    private final MovieRepo repo;
    private final ReviewRepo reviewRepo;
    private final GenreRepo genreRepo;

    public MovieService(MovieRepo repo, ReviewRepo reviewRepo, GenreRepo genreRepo) {
        this.repo = repo;
        this.reviewRepo = reviewRepo;
        this.genreRepo = genreRepo;
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
        if (upd.genresIds() != null) {
            genreRepo.findAllById(List.of(upd.genresIds())).forEach(g -> existing.addGenre(g));
        }
        return toModel(repo.save(existing));
    }

    public EntityModel<GetMovieDto> save(MovieDto nMovie) {
        var movie = Movie.builder()
                .title(nMovie.title())
                .synopsis(nMovie.synopsis())
                .releaseDate(nMovie.releaseDate())
                .genres(nMovie.genresIds() != null ? genreRepo.findAllById(List.of(nMovie.genresIds())) : List.of())
                .build();

        if (nMovie.reviewsIds() != null) {
            reviewRepo.findAllById(List.of(nMovie.reviewsIds())).forEach(r -> r.associateToMovie(movie));
        }

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
