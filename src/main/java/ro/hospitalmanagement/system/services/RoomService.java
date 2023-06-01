package ro.hospitalmanagement.system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.hospitalmanagement.system.dtos.RoomDTO;
import ro.hospitalmanagement.system.entities.RoomEntity;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.repositories.RoomRepository;

@Service
@Slf4j
public class RoomService {
    @Autowired
    RoomRepository roomRepository;

    public RoomEntity add(RoomDTO roomDTO) {
        log.info("Add new room");
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomNumber(roomDTO.getNumber());
        roomEntity.setRoomType(roomDTO.getRoomType());
        roomEntity.setAvailable(true);
        log.info("Saving room to database");
        RoomEntity room = roomRepository.save(roomEntity);
        log.info("Room successfully saved");
        return room;
    }

    public Iterable<RoomEntity> findAll() {
        log.info("Find all rooms");
        return this.roomRepository.findAll();
    }

    public void delete(int id) throws InexistentResourceException {
        log.info("Search for the room you want to delete by id");
        this.roomRepository.findById(id).orElseThrow(() -> new InexistentResourceException("This room does not exist, room id:"));
        log.info("The room to delete has been found and will be deleted ");
        this.roomRepository.deleteById(id);
        log.info("The room has been successfully deleted");

    }
}