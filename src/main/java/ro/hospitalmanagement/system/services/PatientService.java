package ro.hospitalmanagement.system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.hospitalmanagement.system.dtos.PatientDTO;
import ro.hospitalmanagement.system.entities.AddressEntity;
import ro.hospitalmanagement.system.entities.PatientEntity;
import ro.hospitalmanagement.system.exceptions.AlreadyExistsException;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.exceptions.NotEditableException;
import ro.hospitalmanagement.system.repositories.AddressRepository;
import ro.hospitalmanagement.system.repositories.PatientRepository;

import java.util.Optional;

@Service
@Slf4j
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public PatientEntity add(PatientDTO patientDTO) throws AlreadyExistsException {
        if (!this.patientRepository.existsByLastNameAndFirstName(patientDTO.getLastName(), patientDTO.getFirstName())) {
            log.info("Add new address");
            AddressEntity address = new AddressEntity();
            address.setCountry(patientDTO.getCountry());
            address.setCity(patientDTO.getCity());
            address.setStreet(patientDTO.getStreet());
            address.setAddress(patientDTO.getAddress());
            log.info("Saving address to database");
            AddressEntity addressEntity = addressRepository.save(address);
            log.info("Address successfully saved");
            log.info("Add new patient");
            PatientEntity patient = new PatientEntity();
            patient.setId(patientDTO.getId());
            patient.setFirstName(patientDTO.getFirstName());
            patient.setLastName(patientDTO.getLastName());
            patient.setEmail(patientDTO.getEmail());
            patient.setGender(patientDTO.getGender());
            patient.setBirthDate(patientDTO.getBirtDate());
            patient.setPhone(patientDTO.getPhone());
            patient.setAddress(addressEntity);
            patient.setEnrollmentDate(patientDTO.getEnrollmentDate());
            patient.setInsuranceNumber(patientDTO.getInsuranceNumber());
            patient.setInsuranceCompany(patientDTO.getInsuranceCompany());
            log.info("Saving patient to database");
            PatientEntity patientEntity = patientRepository.save(patient);
            log.info("Patient successfully saved");
            return patientEntity;
        } else throw new AlreadyExistsException("Patient already exists in data base!");
    }

    public Iterable<PatientEntity> findAll() {
        log.info("Find all patients");
        return this.patientRepository.findAll();
    }

    public void delete(int id) throws InexistentResourceException {
        log.info("Search for the patient you want to delete by id");
        this.patientRepository.findById(id).orElseThrow(() -> new InexistentResourceException("this patient does not exist in data base"));
        log.info("The patient to delete has been found and will be deleted ");
        this.patientRepository.deleteById(id);
        log.info("The patient has been successfully deleted");
    }

    public Optional<PatientEntity> search(String name, String firstname) throws InexistentResourceException {
        log.info("Search for the patient name");
        return this.patientRepository.findByLastNameAndFirstName(name, firstname);
    }

    @Transactional
    public PatientEntity updatePartial(int id, PatientDTO patientDTO) throws InexistentResourceException, NotEditableException {
        Optional<PatientEntity> optionalPatient = this.patientRepository.findById(id);
        Optional<AddressEntity> optionalAddress = this.patientRepository.findAddress(id);
        if (!optionalPatient.isPresent()) {
            throw new InexistentResourceException("Patient does not exists");
        }
        PatientEntity patient = optionalPatient.get();
        AddressEntity address = optionalAddress.get();
        if (patientDTO.getFirstName() != null || patientDTO.getLastName() != null || patientDTO.getBirtDate() != null || patientDTO.getGender() != null || patientDTO.getInsuranceNumber() != null || patientDTO.getInsuranceCompany() != null || patientDTO.getEnrollmentDate() != null) {
            throw new NotEditableException("You can modify just phone, email, address");
        }
        if (patientDTO.getPhone() != null) {
            patient.setPhone(patientDTO.getPhone());
        }
        if (patientDTO.getEmail() != null) {
            patient.setEmail(patientDTO.getEmail());
        }
        if (patientDTO.getCountry() != null) {
            address.setCountry(patientDTO.getCountry());
        }
        if (patientDTO.getCity() != null) {
            address.setCity(patientDTO.getCity());
        }
        if (patientDTO.getStreet() != null) {
            address.setStreet(patientDTO.getStreet());
        }
        if (patientDTO.getAddress() != null) {
            address.setAddress(patientDTO.getAddress());
        }
        patient.setAddress(address);
        this.patientRepository.save(patient);
        return patient;
    }
}