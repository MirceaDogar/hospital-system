package ro.hospitalmanagement.system.repositories;

import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.ProceduresEntity;

import java.util.Optional;

public interface ProceduresRepository extends CrudRepository<ProceduresEntity, Integer> {
    Optional <ProceduresEntity> findByName(String procedureName);

}