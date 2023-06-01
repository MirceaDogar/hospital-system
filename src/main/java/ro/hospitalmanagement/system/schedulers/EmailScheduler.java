package ro.hospitalmanagement.system.schedulers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.hospitalmanagement.system.entities.AppointmentEntity;
import ro.hospitalmanagement.system.repositories.AppointmentRepository;
import ro.hospitalmanagement.system.services.EmailService;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class EmailScheduler {
    @Autowired
    EmailService emailService;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Scheduled(cron = "0 0 8 * * *")
    public void remainderAppointment() throws MessagingException {
        log.trace("Fetching all appointments...");
        Iterable<AppointmentEntity>  appointmentEntities = this.appointmentRepository.findAll();
        log.trace("Appointments fetched successfully.");
        for (AppointmentEntity appointment : appointmentEntities){
            log.trace("Get appointments from database.");
            log.trace("Get appointments datetime.");
            if (appointment.getStartAppointment().isAfter(LocalDateTime.now().plusDays(1))&appointment.getStartAppointment().isBefore(LocalDateTime.now().plusDays(2))){
                log.trace("Sending email....");
                this.emailService.sendEmail(appointment.getPatient().getEmail(),"Appointment remainder", "Your appointment is in less then 24h!");
                log.trace("Email sent successfully");
            }
        }
    }

}