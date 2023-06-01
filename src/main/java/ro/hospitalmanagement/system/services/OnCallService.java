package ro.hospitalmanagement.system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.hospitalmanagement.system.dtos.OnCallDTO;
import ro.hospitalmanagement.system.entities.DoctorEntity;
import ro.hospitalmanagement.system.entities.NurseEntity;
import ro.hospitalmanagement.system.entities.OnCallEntity;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.repositories.DoctorRepository;
import ro.hospitalmanagement.system.repositories.NurseRepository;
import ro.hospitalmanagement.system.repositories.OnCallRepository;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
@Slf4j
public class OnCallService {
    @Autowired
    OnCallRepository onCallRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    NurseRepository nurseRepository;
    @Autowired
    EmailService emailService;

    @Transactional
    public OnCallEntity add(OnCallDTO onCallDTO) throws InexistentResourceException, MessagingException {
        log.info("Add new on call");
        OnCallEntity onCallEntity = new OnCallEntity();
        log.trace("Search for the doctor");
        Optional<DoctorEntity> doctorOptional =
                doctorRepository.findByLastNameAndFirstName(onCallDTO.getDoctorLastName(), onCallDTO.getDoctorFirstName());
        if (!doctorOptional.isPresent()) {
            throw new InexistentResourceException("This doctor does not exist!", onCallDTO.getDoctorLastName(), onCallDTO.getDoctorFirstName());
        }
        log.trace("Doctor found");
        log.trace("Search for the nurse");
        Optional<NurseEntity> nurseOptional =
                nurseRepository.findByLastNameAndFirstName(onCallDTO.getNurseLastName(), onCallDTO.getNurseFirstName());
        if (!nurseOptional.isPresent()) {
            throw new InexistentResourceException("This nurse does not exist!", onCallDTO.getNurseLastName(), onCallDTO.getNurseFirstName());
        }
        log.trace("Nurse found");
        log.trace("Get doctor and nurse");
        NurseEntity nurse = nurseOptional.get();
        DoctorEntity doctor = doctorOptional.get();
        log.trace("Adding doctor and nurse");
        onCallEntity.setDoctor(doctor);
        onCallEntity.setNurse(nurse);
        onCallEntity.setStarOnCall(onCallDTO.getStarOnCall());
        onCallEntity.setEndOnCall(onCallDTO.getEndOnCall());
        log.info("On call successfully added");
        log.info("sending emails...");
        this.emailService.sendEmail(doctor.getEmail(), "On call", "The following on call service for you has been added, check the application for details");
        this.emailService.sendEmail(nurse.getEmail(), "On call", "The following on call service for you has been added, check the application for details");
        OnCallEntity onCall = onCallRepository.save(onCallEntity);
        log.trace("On call has been successfully saved");
        return onCall;
    }

    public Iterable<OnCallEntity> findAll() {
        log.info("Find all on call");
        return this.onCallRepository.findAll();
    }

    public void delete(int id) throws InexistentResourceException {
        log.info("Search for the on call you want to delete by id");
        this.onCallRepository.findById(id).orElseThrow(() -> new InexistentResourceException("Does not exist!"));
        log.info("The on call to delete has been found and will be deleted ");
        this.onCallRepository.deleteById(id);
        log.info("The on call has been successfully deleted");
    }
}