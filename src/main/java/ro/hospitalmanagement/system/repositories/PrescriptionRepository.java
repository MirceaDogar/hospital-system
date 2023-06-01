package ro.hospitalmanagement.system.repositories;

import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.MedicationEntity;
import ro.hospitalmanagement.system.entities.PatientEntity;
import ro.hospitalmanagement.system.entities.PrescriptionEntity;

import java.util.Optional;


public interface PrescriptionRepository extends CrudRepository<PrescriptionEntity, Integer> {


    PrescriptionEntity findByPatientEntityAndMedicationEntity(Optional<PatientEntity> byId, Optional<MedicationEntity> byId1);
}