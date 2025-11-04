package br.com.fiap.cinefinder.dto;

import br.com.fiap.cinefinder.model.Review;

public record GetReviewDto(
        Long id,
        String title,
        String comments,
        MovieRefDto movie,
        GetUserDto author,
        String localization,
        Double rate) {

    public static GetReviewDto fromReview(Review r) {
        return new GetReviewDto(
                r.getId(),
                r.getTitle(),
                r.getComments(),
                MovieRefDto.fromMovie(r.getMovie()),
                GetUserDto.fromAppUser(r.getAuthor()),
                r.getLocalization(),
                r.getRate());
    }

}
