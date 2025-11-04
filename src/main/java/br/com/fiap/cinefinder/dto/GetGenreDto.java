package br.com.fiap.cinefinder.dto;

import java.util.List;

import br.com.fiap.cinefinder.model.Genre;

public record GetGenreDto(
        Long id,
        String name,
        List<MovieRefDto> movie) {

    public static GetGenreDto fromGenre(Genre genre) {
        return new GetGenreDto(
                genre.getId(),
                genre.normalizeName(),
                genre.getMovies().stream()
                        .map(MovieRefDto::fromMovie)
                        .toList());
    }

}
