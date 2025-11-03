package br.com.fiap.cinefinder.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ReviewDto(
        @NotBlank String title,

        @Max(2000) String comments,

        @PositiveOrZero @Min(0) @Max(10) Double rate,

        @NotBlank String localization,

        @NotNull Long authorId,

        @NotNull Long movieId) {
}
