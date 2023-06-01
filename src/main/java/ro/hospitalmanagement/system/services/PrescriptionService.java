package ro.hospitalmanagement.system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.hospitalmanagement.system.dtos.PrescriptionDTO;
import ro.hospitalmanagement.system.entities.*;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.repositories.DoctorRepository;
import ro.hospitalmanagement.system.repositories.MedicationRepository;
import ro.hospitalmanagement.system.repositories.PatientRepository;
import ro.hospitalmanagement.system.repositories.PrescriptionRepository;

import java.util.Optional;

@Service
@Slf4j
public class PrescriptionService {
    @Autowired
    PrescriptionRepository prescriptionRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    MedicationRepository medicationRepository;
    @Autowired
    PatientRepository patientRepository;


    @Transactional
    public PrescriptionEntity add(PrescriptionDTO prescriptionDTO) throws InexistentResourceException {
        log.info("Add prescription");
        PrescriptionEntity prescriptionEntity = new PrescriptionEntity();
        log.info("Search for doctor");
        Optional<DoctorEntity> doctor = this.doctorRepository.findByLastNameAndFirstName(prescriptionDTO.getDoctorLastname(), prescriptionDTO.getDoctorFirstname());
        log.info("Search for patient");
        Optional<PatientEntity> patient = this.patientRepository.findByLastNameAndFirstName(prescriptionDTO.getPatientLastname(), prescriptionDTO.getPatientFirstname());
        log.info("Search for medication");
        Optional<MedicationEntity> medication = this.medicationRepository.findByName(prescriptionDTO.getMedicationName());
        if (doctor.isEmpty()) {
            log.warn("Doctor not found!");
            throw new InexistentResourceException("Doctor not found!");
        }
        if (patient.isEmpty()) {
            log.warn("Patient not found!");
            throw new InexistentResourceException("Patient not found!");
        }
        if (medication.isEmpty()) {
            log.warn("Medication not found!");
            throw new InexistentResourceException("Medication not found!");
        }
        log.trace("Create PK for prescription");
        PrescriptionPK prescriptionPK = new PrescriptionPK(doctor.get().getId(), medication.get().getId());
        prescriptionEntity.setPrescriptionPK(prescriptionPK);
        prescriptionEntity.setPatientEntity(patient.get());
        prescriptionEntity.setMedicationEntity(medication.get());
        prescriptionEntity.setDoctorId(doctor.get());
        log.trace("Saving prescription");
        return prescriptionRepository.save(prescriptionEntity);
    }

    public Iterable<PrescriptionEntity> findAll() {
        log.info("Find all prescriptions");
        return this.prescriptionRepository.findAll();
    }

    public void delete(int id, int id2) throws InexistentResourceException {
        Optional<PrescriptionEntity> prescriptionEntity = Optional.ofNullable(this.prescriptionRepository.findByPatientEntityAndMedicationEntity(patientRepository.findById(id), medicationRepository.findById(id2)));
        if (prescriptionEntity.isEmpty()) {
            throw new InexistentResourceException("This prescription does not exist");
        }
        this.prescriptionRepository.deleteById(id);
    }
}