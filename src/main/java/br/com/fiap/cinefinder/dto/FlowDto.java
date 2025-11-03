package br.com.fiap.cinefinder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FlowDto(
    @NotBlank String title,
    @NotNull Long authorId,
    Long[] movieIds

) {
    
}
