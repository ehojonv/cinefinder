package br.com.fiap.cinefinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.fiap.cinefinder.model.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

}
