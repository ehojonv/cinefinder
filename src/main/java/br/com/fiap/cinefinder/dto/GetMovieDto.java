package br.com.fiap.cinefinder.dto;

import java.time.LocalDate;
import java.util.List;

import br.com.fiap.cinefinder.model.Movie;

public record GetMovieDto(
        Long id,
        String title,
        String synopsis,
        LocalDate releaseDate,
        Double rating,
        List<String> genres,
        Integer numberOfReviews) {

    public static GetMovieDto fromMovie(Movie m) {
        return new GetMovieDto(
                m.getId(),
                m.getTitle(),
                m.getSynopsis(),
                m.getReleaseDate(),
                m.getRating(),
                m.getGenreNames(),
                m.getReviews().size());
    }

}
