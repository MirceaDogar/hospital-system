package ro.hospitalmanagement.system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.hospitalmanagement.system.dtos.PersonDTO;
import ro.hospitalmanagement.system.entities.AddressEntity;
import ro.hospitalmanagement.system.entities.PersonEntity;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.repositories.AddressRepository;
import ro.hospitalmanagement.system.repositories.PersonRepository;

import java.util.List;

@Service
@Slf4j
public class PersonService {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    AddressRepository addressRepository;

    @Transactional
    public PersonEntity add(PersonDTO person) {
        log.info("Add new address");
        AddressEntity address = new AddressEntity();
        address.setCity(person.getCity());
        address.setCountry(person.getCountry());
        address.setAddress(person.getAddress());
        address.setStreet(person.getStreet());
        log.info("Saving address to database");
        AddressEntity addressEntity = addressRepository.save(address);
        log.info("Address successfully saved");
        log.info("Add new person");
        PersonEntity personEntity = new PersonEntity();
        personEntity.setFirstName(person.getFirstName());
        personEntity.setId(person.getId());
        personEntity.setLastName(person.getLastName());
        personEntity.setEmail(person.getEmail());
        personEntity.setGender(person.getGender());
        personEntity.setPhone(person.getPhone());
        personEntity.setAddress(addressEntity);
        personEntity.setBirthDate(person.getBirtDate());
        log.info("Saving person to database");
        personRepository.save(personEntity);
        log.info("Person successfully saved");
        return personEntity;
    }

    public Iterable<PersonEntity> findAll() {
        log.info("Find all persons");
        return this.personRepository.findAll();
    }

    public void delete(int id) throws InexistentResourceException {
        log.info("Search for the person you want to delete by id");
        this.personRepository.findById(id).orElseThrow(() -> new InexistentResourceException("Person does not exist"));
        log.info("The person to delete has been found and will be deleted ");
        this.personRepository.deleteById(id);
        log.info("The person has been successfully deleted");
    }

    public PersonEntity findById(int id) throws InexistentResourceException{
        log.info("Search the person by id{}", id);
        return this.personRepository.findById(id).orElseThrow(()-> new InexistentResourceException("Person does not exist"));
    }
    public List<PersonEntity> searchByName(String name){
        if (name == null || name.isEmpty()){
            return null;
        }
        return this.personRepository.findByLastName(name);
    }
}