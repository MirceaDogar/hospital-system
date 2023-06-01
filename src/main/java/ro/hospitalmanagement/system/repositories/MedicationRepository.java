package ro.hospitalmanagement.system.repositories;

import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.MedicationEntity;

import java.util.Optional;

public interface MedicationRepository extends CrudRepository<MedicationEntity, Integer> {

    Optional<MedicationEntity> findByName(String medicationName);
}