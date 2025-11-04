package br.com.fiap.cinefinder.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record MovieDto(
        @NotBlank String title,
        @NotBlank @Size(min = 10, max = 2000) String synopsis,
        @PastOrPresent LocalDate releaseDate,
        @PositiveOrZero @DecimalMin("0.0") @DecimalMax("10.0") Long rating,
        Long[] reviewsIds,
        Long[] genresIds

) {

}
