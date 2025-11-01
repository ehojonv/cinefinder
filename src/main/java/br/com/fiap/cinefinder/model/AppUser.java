package br.com.fiap.cinefinder.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.cinefinder.validation.Password;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor

@Entity
@Table(name = "cf_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @Email
    private String email;

    @Password
    private String password;

    @PastOrPresent
    private LocalDate dateOfBirth;

    @Default
    @OneToMany(mappedBy = "author")
    private List<Review> reviews = new ArrayList<>();

    @Default
    @OneToMany(mappedBy = "author")
    private List<Flow> flows = new ArrayList<>();

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    public void addFlow(Flow flow) {
        this.flows.add(flow);
    }

    public void removeFlow(Flow flow) {
        this.flows.remove(flow);
    }

}
