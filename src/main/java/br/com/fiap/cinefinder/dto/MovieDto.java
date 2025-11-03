package br.com.fiap.cinefinder.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record MovieDto(
                @NotBlank String title,
                @NotBlank @Size(min = 10, max = 2000) String synopsis,
                @PastOrPresent LocalDate releaseDate,
                @PositiveOrZero @Min(0) @Max(10) Double rating,
                Long[] reviewsIds,
                Long[] genresIds

) {

}
