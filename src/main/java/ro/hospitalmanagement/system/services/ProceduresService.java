package ro.hospitalmanagement.system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.hospitalmanagement.system.dtos.ProceduresDTO;
import ro.hospitalmanagement.system.entities.ProceduresEntity;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.repositories.ProceduresRepository;

@Service
@Slf4j
public class ProceduresService {
    @Autowired
    ProceduresRepository proceduresRepository;

    public ProceduresEntity add(ProceduresDTO proceduresDTO){
        log.info("Add new procedure");
        ProceduresEntity proceduresEntity = new ProceduresEntity();
        proceduresEntity.setCost(proceduresDTO.getCost());
        proceduresEntity.setName(proceduresDTO.getName());
        log.info("Saving procedure to database");
        ProceduresEntity procedures= proceduresRepository.save(proceduresEntity);
        log.info("procedure successfully saved");
        return procedures;
    }
    public Iterable<ProceduresEntity> findAll(){
        log.info("Find all procedure");
        return this.proceduresRepository.findAll();
    }
    public void delete(int id) throws InexistentResourceException {
        log.info("Search for the procedure you want to delete by id");
        this.proceduresRepository.findById(id).orElseThrow(() -> new InexistentResourceException("this procedure does not exist in data base"));
        log.info("The procedure to delete has been found and will be deleted ");
        this.proceduresRepository.deleteById(id);
        log.info("The procedure has been successfully deleted");

    }


}