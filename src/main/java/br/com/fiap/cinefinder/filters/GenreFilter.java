package br.com.fiap.cinefinder.filters;

public record GenreFilter(
    String name,
    Long[] moviesIds
) {
    
}
