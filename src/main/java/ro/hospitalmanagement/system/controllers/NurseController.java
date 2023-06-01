package ro.hospitalmanagement.system.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.hospitalmanagement.system.dtos.*;
import ro.hospitalmanagement.system.entities.*;
import ro.hospitalmanagement.system.exceptions.AlreadyExistsException;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.exceptions.NotEditableException;
import ro.hospitalmanagement.system.services.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/nurse")
@Slf4j
@Validated
public class NurseController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private NurseService nurseService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ProceduresService proceduresService;
    @Autowired
    private MedicationService medicationService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PersonService personService;

    @Autowired
    private HistoryPatientService historyPatientService;


    @PreAuthorize("hasRole('NURSE') or hasRole('ADMIN')")
    @Operation(summary = "Search person by lastname")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PersonListDTO.class)))
    @GetMapping("/searchPerson")
    public ResponseEntity<PersonListDTO> searchPerson(@RequestParam("name") String name) {
        List<PersonEntity> entityList = this.personService.searchByName(name);
        List<PersonDTO> personDTOList = PersonDTO.from(entityList);
        return new ResponseEntity<>(new PersonListDTO(personDTOList), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('NURSE') or hasRole('DOCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Search patient by first name and lastname")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PatientDTO.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = PatientEntity.class)))
    @GetMapping("/searchPatient")
    public ResponseEntity<PatientDTO> searchPatient(@RequestParam("name") String name, @RequestParam("firstname") String firstname) throws InexistentResourceException {
        log.debug("search patient");
        PatientEntity entity = this.patientService.search(name, firstname).get();
        return new ResponseEntity<>(PatientDTO.from(entity), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('NURSE') or hasRole('ADMIN')")
    @Operation(summary = "Create room")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = RoomDTO.class)))
    @PostMapping("/room")
    public ResponseEntity<RoomDTO> create(@Valid @RequestBody RoomDTO roomDTO) {
        log.debug("Create room");
        RoomEntity roomEntity = this.roomService.add(roomDTO);
        return new ResponseEntity<>(RoomDTO.from(roomEntity), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('NURSE') or hasRole('ADMIN')")
    @Operation(summary = "Create procedures")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = ProceduresDTO.class)))
    @PostMapping("/procedures")
    public ResponseEntity<ProceduresDTO> create(@Valid @RequestBody ProceduresDTO proceduresDTO) {
        log.debug("Create procedures");
        ProceduresEntity proceduresEntity = this.proceduresService.add(proceduresDTO);
        return new ResponseEntity<>(proceduresDTO.from(proceduresEntity), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('NURSE') or hasRole('ADMIN')")
    @Operation(summary = "Create medication")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = MedicationDTO.class)))
    @PostMapping("/medication")
    public ResponseEntity<MedicationDTO> create(@Valid @RequestBody MedicationDTO medicationDTO) {
        log.debug("Create medication");
        MedicationEntity medicationEntity = this.medicationService.add(medicationDTO);
        return new ResponseEntity<>(medicationDTO.from(medicationEntity), HttpStatus.CREATED);

    }
    @PreAuthorize("hasRole('NURSE')")
    @Operation(summary = "Create history")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = HistoryDTO.class)))
    @PostMapping("/history")
    public ResponseEntity<HistoryDTO> create(@Valid @RequestBody HistoryDTO historyDTO) throws AlreadyExistsException {
        log.debug("create history");
        HistoryEntity historyEntity = this.historyService.add(historyDTO);
        return new ResponseEntity<>(historyDTO.from(historyEntity), HttpStatus.CREATED);

    }
    @PreAuthorize("hasRole('NURSE') or hasRole('DOCTOR')")
    @Operation(summary = "Create history patient")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = HistoryPatientDTO.class)))
    @PostMapping("/histoypatient")
    public ResponseEntity<HistoryPatientDTO> create(@Valid @RequestBody HistoryPatientDTO historyPatientDTO) throws InexistentResourceException {
        log.debug("Create patient history");
        this.historyPatientService.addHistory(historyPatientDTO);
        return new ResponseEntity<>(historyPatientDTO, HttpStatus.CREATED);

    }
    @PreAuthorize("hasRole('NURSE') or hasRole('DOCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Delete appointment by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("appointment/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("id") int id) {
        log.info("Delete appointment");
        try {
            this.appointmentService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('NURSE') or hasRole('ADMIN')")
    @Operation(summary = "Edit nurse")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = NurseDTO.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = Void.class)))
    @PatchMapping("/updateNurse/{id}")
    public ResponseEntity<NurseDTO> updateNursePartial(@PathVariable("id") int id, @RequestBody NurseDTO nurseDTO) throws NotEditableException, InexistentResourceException {
        NurseEntity nurseEntity = this.nurseService.updatePartial(id, nurseDTO);
        NurseDTO responseDTO = NurseDTO.from(nurseEntity);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}


