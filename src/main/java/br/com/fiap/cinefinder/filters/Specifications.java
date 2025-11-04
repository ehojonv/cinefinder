package br.com.fiap.cinefinder.filters;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.cinefinder.model.AppUser;
import br.com.fiap.cinefinder.model.Genre;
import br.com.fiap.cinefinder.model.Review;

public class Specifications {

    public static Specification<AppUser> buildUser(UserFilter filter) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (filter.username() != null) {
                predicates = cb.and(
                        predicates,
                        cb.like(
                                cb.lower(root.get("username")),
                                "%" + filter.username().toLowerCase() + "%"));
            }

            if (filter.minAge() != null) {
                var today = java.time.LocalDate.now();
                var maxDateOfBirth = today.minusYears(filter.minAge());
                predicates = cb.and(
                        predicates,
                        cb.lessThanOrEqualTo(root.get("dateOfBirth"), maxDateOfBirth));
            }

            if (filter.maxAge() != null) {
                var today = java.time.LocalDate.now();
                var minDateOfBirth = today.minusYears(filter.maxAge() + 1).plusDays(1);
                predicates = cb.and(
                        predicates,
                        cb.greaterThanOrEqualTo(root.get("dateOfBirth"), minDateOfBirth));
            }

            return predicates;
        };
    }

    public static Specification<Review> buildReview(ReviewFilter filter) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (filter.title() != null) {
                predicates = cb.and(
                        predicates,
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + filter.title().toLowerCase() + "%"));
            }

            if (filter.username() != null) {
                predicates = cb.and(
                        predicates,
                        cb.like(
                                cb.lower(root.get("author").get("username")),
                                "%" + filter.username().toLowerCase() + "%"));
            }

            if (filter.minRating() != null) {
                predicates = cb.and(
                        predicates,
                        cb.greaterThanOrEqualTo(root.get("rate"),
                                filter.minRating().doubleValue()));
            }

            if (filter.maxRating() != null) {
                predicates = cb.and(
                        predicates,
                        cb.lessThanOrEqualTo(root.get("rate"),
                                filter.maxRating().doubleValue()));
            }

            if (filter.localization() != null) {
                predicates = cb.and(
                        predicates,
                        cb.like(
                                cb.lower(root.get("localization")),
                                "%" + filter.localization().toLowerCase() + "%"));
            }

            if (filter.movieTitle() != null) {
                predicates = cb.and(
                        predicates,
                        cb.like(
                                cb.lower(root.get("movie").get("title")),
                                "%" + filter.movieTitle().toLowerCase() + "%"));
            }

            return predicates;
        };
    }

    public static Specification<Genre> buildGenre(GenreFilter filter) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (filter.name() != null) {
                predicates = cb.and(
                        predicates,
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + filter.name().toLowerCase() + "%"));
            }

            if (filter.moviesIds() != null) {
                predicates = cb.and(
                        predicates,
                        root.join("cf_movies").get("id").in(List.of(filter.moviesIds())));
            }
            
            return predicates;
        };
    }

}
