package ro.hospitalmanagement.system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.hospitalmanagement.system.dtos.HistoryPatientDTO;
import ro.hospitalmanagement.system.entities.HistoryEntity;
import ro.hospitalmanagement.system.entities.PatientEntity;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.repositories.HistoryRepository;
import ro.hospitalmanagement.system.repositories.PatientRepository;

import java.util.Optional;

@Service
@Slf4j
public class HistoryPatientService {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    HistoryRepository historyRepository;

    @Transactional
    public void addHistory(HistoryPatientDTO historyPatientDTO) throws InexistentResourceException {
        log.info("Add new patient history");
        log.info("Check if patient exist");
        Optional<PatientEntity> patientOptional = patientRepository.findByLastNameAndFirstName(historyPatientDTO.getLastName(), historyPatientDTO.getFirstName());
        if (!patientOptional.isPresent()) {
            throw new InexistentResourceException("Patient does not exist", historyPatientDTO.getLastName(), historyPatientDTO.getFirstName());
        }
        log.info("Patient found");
        log.info("Check if history exist in db");
        Optional<HistoryEntity> historyOptional = historyRepository.findByMedicalHistory(historyPatientDTO.getMedicalHistory());
        if (!historyOptional.isPresent()) {
            throw new InexistentResourceException("Can not found medical history", historyPatientDTO.getMedicalHistory());
        }
        log.info("History found");
        log.info("Get history");
        HistoryEntity history = historyOptional.get();
        log.info("Get patient");
        PatientEntity patient = patientOptional.get();
        log.info("Adding the patient history");
        patient.getHistoryEntity().add(history);
        history.getPatientEntities().add(patient);
        log.info("Saving the patient history");
        historyRepository.save(history);
        log.info("The patient history has been successfully added");
    }

    public Iterable<HistoryEntity> findAll() {
        log.info("find all patient history");
        return this.historyRepository.findAll();
    }

    public void delete(int id) {
        this.historyRepository.deleteById(id);
    }
}