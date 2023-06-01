package ro.hospitalmanagement.system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.hospitalmanagement.system.dtos.AddressDTO;
import ro.hospitalmanagement.system.entities.AddressEntity;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.repositories.AddressRepository;

@Service
@Slf4j
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Transactional
    public AddressEntity add(AddressDTO addressDTO) {
        log.info("Add new address");
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setCountry(addressDTO.getCountry());
        addressEntity.setCity(addressDTO.getCity());
        addressEntity.setStreet(addressDTO.getStreet());
        addressEntity.setAddress(addressDTO.getAddress());
        log.info("Saving address to database");
        AddressEntity address = addressRepository.save(addressEntity);
        log.info("Address successfully saved");
        return address;

    }

    public Iterable<AddressEntity> findAll() {
        log.info("Find all addresses");
        return this.addressRepository.findAll();
    }

    public void delete(int id) throws InexistentResourceException {
        log.info("Search for the address you want to delete by id");
        this.addressRepository.findById(id).orElseThrow(() -> new InexistentResourceException("This address does not exist!"));
        log.info("The address to delete has been found and will be deleted deleted");
        this.addressRepository.deleteById(id);
        log.info("The address has been successfully deleted");
    }
}