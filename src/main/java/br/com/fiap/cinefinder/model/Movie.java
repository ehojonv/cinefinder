package br.com.fiap.cinefinder.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Size(min = 10, max = 2000)
    private String synopsis;

    @Default
    @ManyToMany
    @JoinTable(
        name = "cf_movie_genres",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genres_id")
    )
    private List<Genre> genres = new ArrayList<Genre>();

    @PastOrPresent
    private LocalDate releaseDate;

    @PositiveOrZero
    @Column(columnDefinition = "NUMERIC(4,2) DEFAULT 0")
    private Double rating;

    @Default
    @OneToMany(mappedBy = "movie")
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
