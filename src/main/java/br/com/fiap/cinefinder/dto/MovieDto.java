package br.com.fiap.cinefinder.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record MovieDto(
        @NotBlank String title,
        @NotBlank @Size(min = 10, max = 2000) String synopsis,
        Long[] genresIds,
        @PastOrPresent LocalDate releaseDate

) {

}
