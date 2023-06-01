package ro.hospitalmanagement.system.schedulers;

import lombok.extern.slf4j.Slf4j;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.hospitalmanagement.system.entities.AppointmentEntity;
import ro.hospitalmanagement.system.entities.DoctorEntity;
import ro.hospitalmanagement.system.repositories.AppointmentRepository;
import ro.hospitalmanagement.system.repositories.DoctorRepository;
import ro.hospitalmanagement.system.services.EmailService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ReportScheduler {
    @Autowired
    private EmailService emailService;
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Scheduled(cron = "0 0 8 * * *")
    public void sendReportToDoctor() throws MessagingException {
        log.info("Send raport to doctors");
        log.trace("Fetching all doctors...");
        Iterable<DoctorEntity> doctorEntityList = this.doctorRepository.findAll();
        log.trace("Doctors fetched successfully.");
        log.debug("Generating reports for all doctors.");
        for (DoctorEntity doctor : doctorEntityList) {
            log.trace("Generate report for doctor with name {}", doctor.getLastName());
            log.trace("Get appointments for each doctor from database.");
            List<AppointmentEntity> appointmentEntityList = this.appointmentRepository.appointmentList(doctor.getId(), LocalDateTime.now(), LocalDateTime.from(LocalDateTime.now().plusDays(1)));
            if (appointmentEntityList.isEmpty()) {
                this.emailService.sendEmail(doctor.getEmail(), "Daily report", "Hello! You do not have any appointments for today!");
            } else {
                try (StringWriter stringWriter = new StringWriter()) {
                    CSVWriter csvWriter = new CSVWriter(stringWriter);
                    StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(csvWriter).build();
                    beanToCsv.write(appointmentEntityList);
                    log.debug("Generating report");
                    String csvReport = stringWriter.toString();
                    log.debug("Generated successfully.");
                    csvWriter.close();
                    log.debug("Sending email to {}", doctor.getEmail());
                    this.emailService.sendEmail(doctor.getEmail(), "Daily report", "Hello! This is your appointment report for today.", csvReport);
                } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
                    log.error("Something exception happened when creating report for doctor with name {} due to {}", doctor.getLastName(), e.getMessage());
                } catch (MessagingException e) {
                    log.error("Something exception happened when sending email to {} due to {}", doctor.getEmail(), e.getMessage());
                }
            }
        }
    }
}