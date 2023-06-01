package ro.hospitalmanagement.system.repositories;

import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.PersonEntity;

import java.util.List;


public interface PersonRepository extends CrudRepository<PersonEntity, Integer> {
    List<PersonEntity> findByLastName(String name);
}