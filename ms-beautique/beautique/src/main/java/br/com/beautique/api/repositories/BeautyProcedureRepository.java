package br.com.beautique.api.repositories;

import br.com.beautique.api.entities.BeautyProceduresEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeautyProcedureRepository extends JpaRepository<BeautyProceduresEntity, Long> {
}
