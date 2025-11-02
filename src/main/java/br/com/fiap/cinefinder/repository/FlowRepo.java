package br.com.fiap.cinefinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.fiap.cinefinder.model.Flow;

@Repository
public interface FlowRepo extends JpaRepository<Flow, Long>, JpaSpecificationExecutor<Flow> {

}
