package ro.hospitalmanagement.system.repositories;

import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.AddressEntity;

public interface AddressRepository extends CrudRepository<AddressEntity, Integer> {

}