package ro.hospitalmanagement.system.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.AddressEntity;
import ro.hospitalmanagement.system.entities.NurseEntity;

import java.util.Optional;

public interface NurseRepository extends CrudRepository<NurseEntity, Integer> {

    NurseEntity findByLastName(String lastname);

    Optional<NurseEntity> findByLastNameAndFirstName(String lastName, String firstName);
    boolean existsByLastNameAndFirstName(String lastName, String firstName);

    @Query("SELECT n.address FROM NurseEntity n WHERE n.id=:id")
    Optional<AddressEntity> findAddress(int id);
}