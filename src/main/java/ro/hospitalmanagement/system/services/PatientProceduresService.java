package ro.hospitalmanagement.system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.hospitalmanagement.system.dtos.PatientProceduresDTO;
import ro.hospitalmanagement.system.entities.*;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.repositories.DoctorRepository;
import ro.hospitalmanagement.system.repositories.PatientProceduresRepository;
import ro.hospitalmanagement.system.repositories.PatientRepository;
import ro.hospitalmanagement.system.repositories.ProceduresRepository;

import java.util.Optional;

@Service
@Slf4j
public class PatientProceduresService {
    @Autowired
    PatientProceduresRepository patientProceduresRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    ProceduresRepository proceduresRepository;

    public PatientProceduresEntity add(PatientProceduresDTO patientProceduresDTO) throws InexistentResourceException {
        log.info("Add new patient procedures");
        PatientProceduresEntity patientProceduresEntity = new PatientProceduresEntity();

        Optional<ProceduresEntity> proceduresOptional = proceduresRepository.findByName(patientProceduresDTO.getProcedureName());
        Optional<PatientEntity> patientOptional = patientRepository.findByLastNameAndFirstName(patientProceduresDTO.getPatientLastName(), patientProceduresDTO.getPatientFirstName());
        Optional<DoctorEntity> doctorOptional = doctorRepository.findByLastNameAndFirstName(patientProceduresDTO.getDoctorLastName(), patientProceduresDTO.getDoctorFirstName());
        log.info("Search for procedure");
        if (!proceduresOptional.isPresent()) {
            log.warn("This procedure does not exist!");
            throw new InexistentResourceException("This procedure does not exist!", patientProceduresDTO.getProcedureName());
        }
        log.info("Procedure exist");
        log.info("Search for patient");
        if (!patientOptional.isPresent()) {
            log.warn("This patient does not exist!");
            throw new InexistentResourceException("This patient does not exist!", patientProceduresDTO.getPatientLastName(), patientProceduresDTO.getPatientFirstName());
        }
        log.info("Patient exist");
        log.info("Search for doctor");
        if (!doctorOptional.isPresent()) {
            log.warn("This doctor does not exist!");
            throw new InexistentResourceException("This doctor does not exist!", patientProceduresDTO.getPatientLastName(), patientProceduresDTO.getPatientFirstName());
        }
        log.info("Patient exist");
        log.info("Getting procedures, patient, doctor");
        ProceduresEntity procedures = proceduresOptional.get();
        PatientEntity patient = patientOptional.get();
        DoctorEntity doctor = doctorOptional.get();

        int patientId = patient.getId();
        int doctorId = doctor.getId();
        int proceduresId = procedures.getId();
        log.info("New PK for patient procedures");
        PatientProceduresPK patientProceduresPK = new PatientProceduresPK(patientId, doctorId, proceduresId);
        patientProceduresEntity.setId(patientProceduresPK);
        patientProceduresEntity.setDoctor(doctor);
        patientProceduresEntity.setPatient(patient);
        patientProceduresEntity.setProcedures(procedures);
        patientProceduresEntity.setDescription(patientProceduresDTO.getDescription());
        log.info("Saving patient procedures");
        return patientProceduresRepository.save(patientProceduresEntity);
    }

    public Iterable<PatientProceduresEntity> findAll() {
        log.info("Find all patient procedures");
        return this.patientProceduresRepository.findAll();
    }

    public Iterable<PatientProceduresEntity> findByPatientId(int id){
        log.info("find patient by id");
        return this.patientProceduresRepository.findByPatientId(id);
    }
}