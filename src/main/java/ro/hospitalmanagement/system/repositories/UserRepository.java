package ro.hospitalmanagement.system.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ro.hospitalmanagement.system.entities.UserEntity;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String email);
    Boolean existsByUsername(String username);

}