package ro.hospitalmanagement.system.schedulers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.hospitalmanagement.system.entities.AppointmentEntity;
import ro.hospitalmanagement.system.entities.RoomEntity;
import ro.hospitalmanagement.system.repositories.RoomRepository;
import ro.hospitalmanagement.system.services.AppointmentService;
import ro.hospitalmanagement.system.services.RoomService;

import java.time.LocalDateTime;

@Component
@Slf4j
public class RoomAvailabilityScheduler {
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    RoomService roomService;
    @Autowired
    RoomRepository roomRepository;

    @Scheduled(fixedRate = 10000)
    public void roomAvailability() {

        Iterable<AppointmentEntity> appointmentEntities = this.appointmentService.findAll();
        Iterable<RoomEntity> roomEntities = this.roomService.findAll();
        for (AppointmentEntity appointment : appointmentEntities)
            for (RoomEntity room : roomEntities) {
                if(room.getRoomNumber() == appointment.getRoom().getRoomNumber() && LocalDateTime.now().isAfter(appointment.getStartAppointment()) && LocalDateTime.now().isBefore(appointment.getEndAppointment())) {
                    room.setAvailable(false);

                } else {
                    room.setAvailable(true);
                }
                roomRepository.save(room);
            }


    }
}