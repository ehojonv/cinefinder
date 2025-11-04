package br.com.fiap.cinefinder.dto;

import jakarta.validation.constraints.NotBlank;

public record GenreDto(
    @NotBlank String name,
    Long[] moviesIds
) {
    
}
