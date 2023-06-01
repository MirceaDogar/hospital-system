package ro.hospitalmanagement.system.repositories;

import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.OnCallEntity;

import java.util.Optional;

public interface OnCallRepository extends CrudRepository<OnCallEntity, Integer> {
}