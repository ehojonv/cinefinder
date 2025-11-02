package br.com.fiap.cinefinder.dto;

import br.com.fiap.cinefinder.model.Genre;

public record GetGenreDto(Long id, String name){

    public static GetGenreDto fromGenre(Genre g){
        if (g == null) return null;
        return new GetGenreDto(g.getId(), g.getName());
    }

}
