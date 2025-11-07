package br.com.fiap.cinefinder.dto;

import br.com.fiap.cinefinder.model.Movie;

public record MovieRefDto(
        Long id,
        String title,
        Double rating) {

    public static MovieRefDto fromMovie(Movie m) {
        return new MovieRefDto(
                m.getId(),
                m.getTitle(),
                m.getRating());
    }
}