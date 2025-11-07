package br.com.fiap.cinefinder.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ReviewDto(
        @NotBlank String title,
        @Size(max = 2000) String comments,
        @PositiveOrZero @NotNull @DecimalMin("0.0") @DecimalMax("10.0") Double rate,
        @NotBlank String localization,
        @NotNull Long authorId,
        @NotNull Long movieId) {
}
