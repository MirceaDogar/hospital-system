package ro.hospitalmanagement.system.repositories;

import org.springframework.data.repository.CrudRepository;
import ro.hospitalmanagement.system.entities.RoomEntity;


public interface RoomRepository extends CrudRepository<RoomEntity, Integer> {
    RoomEntity findByRoomNumber(int roomNo);
}