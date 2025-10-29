package br.com.fiap.cinefinder.model.movie;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.cinefinder.model.genre.Genre;
import br.com.fiap.cinefinder.model.review.Review;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "cf_movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private String synopsis;

    @Default
    private List<Genre> genres = new ArrayList<Genre>();

    @PastOrPresent
    private LocalDate releaseDate;

    @PositiveOrZero
    private Double rating;

    @Default
    private List<Review> reviews = new ArrayList<Review>();

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    public void calculateRating() {
        this.reviews.stream()
                .mapToDouble(Review::getRate)
                .average()
                .ifPresentOrElse(
                        avg -> this.rating = avg,
                        () -> this.rating = 0.0);
    }

    public List<String> getGenreNames() {
        return this.genres.stream()
                .map(Genre::getName)
                .toList();
    }
}
