package br.com.fiap.cinefinder.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "cf_genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    public void normalizeName() {
        this.name = this.name.trim().toLowerCase();
    }
}
