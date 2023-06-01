package ro.hospitalmanagement.system.repositories;

import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.HistoryEntity;

import java.util.Optional;

public interface HistoryRepository extends CrudRepository<HistoryEntity,Integer> {
    Optional <HistoryEntity> findByMedicalHistory(String code);
    boolean existsByMedicalHistory(String medicalHistory);
}