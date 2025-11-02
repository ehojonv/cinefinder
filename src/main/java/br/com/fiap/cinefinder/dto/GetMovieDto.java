package br.com.fiap.cinefinder.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

import br.com.fiap.cinefinder.model.Movie;

public record GetMovieDto(
        Long id,
        String title,
        String synopsis,
        LocalDate releaseDate,
        Double rating,
        List<GetGenreDto> genres
) {

    public static GetMovieDto fromMovie(Movie m) {
        if (m == null) return null;
    List<GetGenreDto> genres = m.getGenres() == null ? Collections.<GetGenreDto>emptyList()
        : m.getGenres().stream().map(GetGenreDto::fromGenre).collect(Collectors.toList());
        return new GetMovieDto(m.getId(), m.getTitle(), m.getSynopsis(), m.getReleaseDate(), m.getRating(), genres);
    }

}
