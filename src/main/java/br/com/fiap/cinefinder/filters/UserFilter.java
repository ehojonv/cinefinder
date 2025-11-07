package br.com.fiap.cinefinder.filters;

public record UserFilter(
        String username,
        Integer minAge,
        Integer maxAge) {

}
