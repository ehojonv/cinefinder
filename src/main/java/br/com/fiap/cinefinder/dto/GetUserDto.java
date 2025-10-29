package br.com.fiap.cinefinder.dto;

import java.time.LocalDate;
import java.util.List;

import br.com.fiap.cinefinder.model.AppUser;

public record GetUserDto(
    String username,
    String email,
    LocalDate birthDate
) {

    private static GetUserDto fromAppUser(AppUser user){
        return new GetUserDto(
            user.getUsername(),
            user.getEmail(),
            user.getDateOfBirth()
        );
    } 

    public static List<GetUserDto> fromAppUserList(List<AppUser> users){
        return users.stream()
            .map(GetUserDto::fromAppUser)
            .toList();
    }
    
}
