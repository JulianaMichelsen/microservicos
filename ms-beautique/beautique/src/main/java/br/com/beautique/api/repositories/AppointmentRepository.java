package br.com.beautique.api.repositories;

import br.com.beautique.api.entities.AppointmentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentsEntity, Long> {
}
