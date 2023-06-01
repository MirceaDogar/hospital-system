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
import ro.hospitalmanagement.system.entities.DoctorEntity;
import ro.hospitalmanagement.system.entities.MedicationEntity;
import ro.hospitalmanagement.system.entities.PatientProceduresEntity;
import ro.hospitalmanagement.system.entities.PrescriptionEntity;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.exceptions.NotEditableException;
import ro.hospitalmanagement.system.services.DoctorService;
import ro.hospitalmanagement.system.services.MedicationService;
import ro.hospitalmanagement.system.services.PatientProceduresService;
import ro.hospitalmanagement.system.services.PrescriptionService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/doctor")
@Slf4j
@Validated
public class DoctorController {
    @Autowired
    private MedicationService medicationService;
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private PatientProceduresService patientProceduresService;
    @Autowired
    private DoctorService doctorService;

    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Create prescription")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = PrescriptionDTO.class)))
    @PostMapping("/prescription")
    public ResponseEntity<PrescriptionDTO> create(@Valid @RequestBody PrescriptionDTO prescriptionDTO) throws InexistentResourceException {
        log.debug("Create prescription");
        PrescriptionEntity prescriptionEntity = this.prescriptionService.add(prescriptionDTO);
        return new ResponseEntity<>(prescriptionDTO.from(prescriptionEntity), HttpStatus.CREATED);
    }



    @PreAuthorize("hasRole('DOCTOR') or hasRole('NURSE')")
    @Operation(summary = "Create patient procedures")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = PatientProceduresDTO.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("/patientprocedures")
    public ResponseEntity<PatientProceduresDTO> create(@Valid @RequestBody PatientProceduresDTO patientProceduresDTO) {
        log.debug("Create patient procedures");
        try {
            PatientProceduresEntity patientProceduresEntity = this.patientProceduresService.add(patientProceduresDTO);
            return new ResponseEntity<>(patientProceduresDTO.from(patientProceduresEntity), HttpStatus.CREATED);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('DOCTOR') or hasRole('NURSE')")
    @Operation(summary = "Get medication")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MedicationListDTO.class)))
    @GetMapping("/medication")
    public ResponseEntity<MedicationListDTO> getAllMedication() {
        Iterable<MedicationEntity> medication = this.medicationService.findAll();
        List<MedicationDTO> medicationDTOList = new ArrayList<>();
        for (MedicationEntity medicationEntity : medication) {
            medicationDTOList.add(MedicationDTO.from(medicationEntity));
        }
        MedicationListDTO medicationListDTO = new MedicationListDTO(medicationDTOList);
        return new ResponseEntity<>(medicationListDTO, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Edit doctor")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = DoctorDTO.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = Void.class)))
    @PatchMapping("/updateDoctor/{id}")
    public ResponseEntity<DoctorDTO> updateDoctorPartial(@PathVariable("id") int id, @RequestBody DoctorDTO doctorDTO) throws InexistentResourceException, NotEditableException {
        DoctorEntity doctorEntity = this.doctorService.updatePartial(id, doctorDTO);
        DoctorDTO responseDTO = DoctorDTO.from(doctorEntity);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}

