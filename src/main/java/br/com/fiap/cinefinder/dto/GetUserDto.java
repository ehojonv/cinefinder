package br.com.fiap.cinefinder.dto;

import java.time.LocalDate;

import br.com.fiap.cinefinder.model.AppUser;

public record GetUserDto(
    Long id,
    String username,
    String email,
    LocalDate birthDate
) {
//aqui
    public static GetUserDto fromAppUser(AppUser user){
        return new GetUserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getDateOfBirth()
        );
    }
    
}
