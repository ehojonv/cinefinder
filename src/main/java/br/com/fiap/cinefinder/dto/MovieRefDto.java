package br.com.fiap.cinefinder.dto;

import br.com.fiap.cinefinder.model.Movie;

public record MovieRefDto(Long id, String title) {

    public static MovieRefDto fromMovie(Movie m) {
        if (m == null) return null;
        return new MovieRefDto(m.getId(), m.getTitle());
    }
}
