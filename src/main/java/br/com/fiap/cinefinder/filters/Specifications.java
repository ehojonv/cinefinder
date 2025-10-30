package br.com.fiap.cinefinder.filters;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.cinefinder.model.AppUser;

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

}
