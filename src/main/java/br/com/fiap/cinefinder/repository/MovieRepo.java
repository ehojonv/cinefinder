package br.com.fiap.cinefinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.fiap.cinefinder.model.Movie;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

}
