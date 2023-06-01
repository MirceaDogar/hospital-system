package ro.hospitalmanagement.system.repositories;

import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.PatientProceduresEntity;
import ro.hospitalmanagement.system.entities.PatientProceduresPK;

public interface PatientProceduresRepository extends CrudRepository<PatientProceduresEntity, PatientProceduresPK> {
    Iterable<PatientProceduresEntity> findByPatientId(int id);
}