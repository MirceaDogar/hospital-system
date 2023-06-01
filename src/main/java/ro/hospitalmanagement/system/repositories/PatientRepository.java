package ro.hospitalmanagement.system.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.AddressEntity;
import ro.hospitalmanagement.system.entities.PatientEntity;

import java.util.Optional;

public interface PatientRepository extends CrudRepository<PatientEntity, Integer> {
    PatientEntity findByLastName(String lastname);

    Optional<PatientEntity> findByLastNameAndFirstName(String lastName, String firstName);
    boolean existsByLastNameAndFirstName(String lastName, String firstName);

    @Query("SELECT p.address FROM PersonEntity p WHERE p.id=:id")
    Optional<AddressEntity> findAddress(int id);
}