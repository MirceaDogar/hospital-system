package ro.hospitalmanagement.system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.hospitalmanagement.system.dtos.NurseDTO;
import ro.hospitalmanagement.system.entities.AddressEntity;
import ro.hospitalmanagement.system.entities.NurseEntity;
import ro.hospitalmanagement.system.exceptions.AlreadyExistsException;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.exceptions.NotEditableException;
import ro.hospitalmanagement.system.repositories.AddressRepository;
import ro.hospitalmanagement.system.repositories.NurseRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class NurseService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private NurseRepository nurseRepository;

    @Transactional
    public NurseEntity add(NurseDTO nurseDTO) throws AlreadyExistsException {
        if (!nurseRepository.existsByLastNameAndFirstName(nurseDTO.getLastName(),nurseDTO.getFirstName())){
            log.info("Add new address");
            AddressEntity address = new AddressEntity();
            address.setCountry(nurseDTO.getCountry());
            address.setCity(nurseDTO.getCity());
            address.setStreet(nurseDTO.getStreet());
            address.setAddress(nurseDTO.getAddress());
            log.trace("Saving address to database");
            AddressEntity addressEntity = addressRepository.save(address);
            log.trace("Address successfully saved");
            log.trace("Add new nurse");
            NurseEntity nurse = new NurseEntity();
            nurse.setId(nurseDTO.getId());
            nurse.setFirstName(nurseDTO.getFirstName());
            nurse.setLastName(nurseDTO.getLastName());
            nurse.setBirthDate(nurseDTO.getBirtDate());
            nurse.setEmail(nurseDTO.getEmail());
            nurse.setPhone(nurseDTO.getPhone());
            nurse.setAddress(addressEntity);
            nurse.setGender(nurseDTO.getGender());
            nurse.setExperience(nurseDTO.getExperience());
            nurse.setDegreeNumber(nurseDTO.getDegreeNumber());
            log.trace("Saving nurse to database");
            NurseEntity nurseEntity = nurseRepository.save(nurse);
            log.info("Nurse successfully saved");
            return nurseEntity;
        }else throw new AlreadyExistsException("This nurse already exist in the database!");
    }
    public Iterable<NurseEntity> findAll(){
        log.info("Find all nurses"); return this.nurseRepository.findAll();
    }
    public void delete(int id) throws InexistentResourceException{
        log.info("Search for the nurse you want to delete by id");
        this.nurseRepository.findById(id).orElseThrow(() -> new InexistentResourceException("Nurse does not exist"));
        log.trace("The nurse to delete has been found and will be deleted ");
        this.nurseRepository.deleteById(id);
        log.info("The nurse has been successfully deleted");
    }

    public NurseEntity updatePartial(int id, NurseDTO nurseDTO) throws InexistentResourceException, NotEditableException {
        log.info("Update nurse by id");
        Optional<NurseEntity> optionalNurse = this.nurseRepository.findById(id);
        Optional<AddressEntity> optionalAddress =  this.nurseRepository.findAddress(id);
        if (!optionalNurse.isPresent()) {
            log.warn("Nurse does not exists");
            throw new InexistentResourceException("Nurse does not exists");
        }
        NurseEntity nurse = optionalNurse.get();
        AddressEntity address = optionalAddress.get();
        log.trace("Check DTO for updating nurse by id");
        if (nurseDTO.getFirstName() != null || nurseDTO.getLastName() != null || nurseDTO.getBirtDate() != null || nurseDTO.getGender() != null || nurseDTO.getDegreeNumber() != null || nurseDTO.getExperience() != null) {
            throw new NotEditableException("You can modify just phone, email, address");
        }
        if (nurseDTO.getPhone() != null) {
            nurse.setPhone(nurseDTO.getPhone());
        }
        if (nurseDTO.getEmail() != null) {
            nurse.setEmail(nurseDTO.getEmail());
        }
        if (nurseDTO.getCountry() != null) {
            address.setCountry(nurseDTO.getCountry());
        }
        if (nurseDTO.getCity() != null) {
            address.setCity(nurseDTO.getCity());
        }
        if (nurseDTO.getStreet() != null) {
            address.setStreet(nurseDTO.getStreet());
        }
        if (nurseDTO.getAddress() != null) {
            address.setAddress(nurseDTO.getAddress());
        }
        nurse.setAddress(address);
        log.info("Nurse updated");
        this.nurseRepository.save(nurse);
        return nurse;
    }
}