package br.com.fiap.cinefinder.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "cf_flow")
public class Flow {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @ManyToOne
    private AppUser author;

    @ManyToMany
    private List<Movie> movies;

    public void addMovie(Movie movie) {
        this.movies.add(movie);
    }

    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
    }

    public void associateToAuthor(AppUser author) {
        this.author = author;
        author.addFlow(this);
    }
    
}
