package ro.hospitalmanagement.system.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ro.hospitalmanagement.system.entities.AppointmentEntity;
import ro.hospitalmanagement.system.entities.DoctorEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends CrudRepository<AppointmentEntity,Integer> {
    AppointmentEntity deleteByStartAppointmentAndEndAppointmentAndDoctor(LocalDate appointmentStart, LocalDate appointmentEnd, DoctorEntity doctor);
    @Query("SELECT a FROM AppointmentEntity a WHERE a.doctor.id=:idDoctor and a.startAppointment>=:todayAppointments and a.startAppointment<=:tomorrowAppointment")
    List<AppointmentEntity> appointmentList(@Param("idDoctor") int idDoctor, LocalDateTime todayAppointments, LocalDateTime tomorrowAppointment);

    List<AppointmentEntity> findByPatientId(int id);
}