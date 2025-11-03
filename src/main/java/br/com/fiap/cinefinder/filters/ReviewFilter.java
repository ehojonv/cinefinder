package br.com.fiap.cinefinder.filters;

public record ReviewFilter(
    String title,
    String username,
    Integer minRating,
    Integer maxRating,
    String localization,
    String movieTitle
) {
    
}
