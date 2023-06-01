package ro.hospitalmanagement.system.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.AddressEntity;
import ro.hospitalmanagement.system.entities.DoctorEntity;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends CrudRepository<DoctorEntity, Integer> {

    Optional<DoctorEntity> findById(int id);

    DoctorEntity findByLastName(String lastname);

    Optional<DoctorEntity> findByLastNameAndFirstName(String lastName, String firstName);

    boolean existsByLastNameAndFirstName(String lastname, String firstname);

    List<DoctorEntity> findByExperienceGreaterThan(int experience);

    List<DoctorEntity> findBySpecialization(String specialization);

    @Query("SELECT d.address FROM DoctorEntity d WHERE d.id=:id")
    Optional<AddressEntity> findAddress(int id);
}