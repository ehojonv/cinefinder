package br.com.fiap.cinefinder.dto;

import java.time.LocalDate;

import br.com.fiap.cinefinder.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

public record UserDto(
        @NotBlank String username,
        @Email String email,
        @Password String password,
        @Past LocalDate dateOfBirth) {

}
