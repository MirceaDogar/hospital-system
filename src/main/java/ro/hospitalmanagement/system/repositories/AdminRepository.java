package ro.hospitalmanagement.system.repositories;

import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.AdminEntity;

public interface AdminRepository extends CrudRepository<AdminEntity,Integer> {
    AdminEntity deleteByFirstNameAndLastName(String firstName, String lastName);
}