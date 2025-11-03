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

import br.com.fiap.cinefinder.dto.GetMovieDto;
import br.com.fiap.cinefinder.dto.MovieDto;
import br.com.fiap.cinefinder.model.Movie;
import br.com.fiap.cinefinder.service.MovieService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("movies")
@Slf4j
public class MovieController {

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping
    public Page<EntityModel<GetMovieDto>> getAllMovies(@PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
        log.info("recuperando todos os movies");
        return service.getAll(pageable);
    }

    @GetMapping("{id}")
    public EntityModel<GetMovieDto> getMovieById(@PathVariable Long id) {
        log.info("recuperando movie pelo id: {}", id);
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = CREATED)
    public EntityModel<GetMovieDto> createMovie(@RequestBody MovieDto movie) {
        log.info("criando novo movie: {}", movie);
        return service.save(movie);
    }

    @PutMapping("{id}")
    public EntityModel<GetMovieDto> updateMovie(@PathVariable Long id, @RequestBody MovieDto upd) {
        log.info("atualizando movie id: {} com os dados: {}", id, upd);
        return service.update(id, upd);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Long id) {
        log.info("deletando movie pelo id: {}", id);
        service.delete(id);
    }

}
