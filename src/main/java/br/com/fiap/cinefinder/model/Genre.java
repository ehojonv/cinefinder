package br.com.fiap.cinefinder.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "cf_genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Default
    @ManyToMany(mappedBy = "genres")
    private List<Movie> movies = new ArrayList<>();

    public String normalizeName() {
        return this.name.trim();
    }
}
