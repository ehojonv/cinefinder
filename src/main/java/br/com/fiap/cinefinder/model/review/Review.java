package br.com.fiap.cinefinder.model.review;

import static jakarta.persistence.GenerationType.IDENTITY;

import br.com.fiap.cinefinder.model.appUser.AppUser;
import br.com.fiap.cinefinder.model.localization.Localization;
import br.com.fiap.cinefinder.model.movie.Movie;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "cf_review")
public class Review {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String comment;

    private Movie movie;

    private AppUser author;

    @Embedded
    private Localization localization;

    @PositiveOrZero
    private Double rate;

    public void associateToMovie(Movie movie) {
        this.movie = movie;
    }

    public void associateToAuthor(AppUser author) {
        this.author = author;
    }

    public String getLocationDetails() {
        return this.localization.getFullAdress();
    }

}
