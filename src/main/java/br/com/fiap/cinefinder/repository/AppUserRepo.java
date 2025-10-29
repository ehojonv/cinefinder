package br.com.fiap.cinefinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.cinefinder.model.AppUser;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    
}
