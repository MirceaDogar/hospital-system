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
import ro.hospitalmanagement.system.entities.AppointmentEntity;
import ro.hospitalmanagement.system.entities.PatientEntity;
import ro.hospitalmanagement.system.entities.PatientProceduresEntity;
import ro.hospitalmanagement.system.exceptions.AlreadyExistsException;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.exceptions.NotAvailableException;
import ro.hospitalmanagement.system.exceptions.NotEditableException;
import ro.hospitalmanagement.system.services.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/patient")
@Slf4j
@Validated
public class PatientController {
    @Autowired
    private PatientProceduresService patientProceduresService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private HistoryService historyService;

    @PreAuthorize("hasRole('PATIENT') or hasRole('NURSE') or hasRole('DOCTOR')")
    @Operation(summary = "Create patient")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = PatientDTO.class)))
    @PostMapping("/patient")
    public ResponseEntity<PatientDTO> create(@Valid @RequestBody PatientDTO patientDTO) throws AlreadyExistsException {
        log.debug("Create patient");
        PatientEntity patientEntity = this.patientService.add(patientDTO);
        return new ResponseEntity<>(PatientDTO.from(patientEntity), HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('PATIENT') or hasRole('NURSE') or hasRole('DOCTOR')")
    @Operation(summary = "Create appointment")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = AppointmentDTO.class)))
    @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = AppointmentDTO.class)))
    @PostMapping("/appointment")
    public ResponseEntity<AppointmentDTO> create(@Valid @RequestBody AppointmentDTO appointmentDTO) throws MessagingException, AlreadyExistsException, NotAvailableException, InexistentResourceException {
        log.debug("Create appointment");
        AppointmentEntity appointmentEntity = this.appointmentService.add(appointmentDTO);
        appointmentService.alreadyExistsAppointment(appointmentDTO);
        log.trace("Sending email");
        this.emailService.sendEmail(appointmentEntity.getPatient().getEmail(), "Your appointment has been registered!",
                "This email is the confirmation of your appointment in the clinic");
        log.trace("Email sent successfully");
        return new ResponseEntity<>(appointmentDTO.from(appointmentEntity), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('PATIENT') or hasRole('NURSE') or hasRole('DOCTOR')")
    @Operation(summary = "Edit patient")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PatientDTO.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = Void.class)))
    @PatchMapping("/updatePatient/{id}")
    public ResponseEntity<PatientDTO> updatePatientPartial(@PathVariable("id") int id, @RequestBody PatientDTO patientDTO) throws NotEditableException, InexistentResourceException {
        PatientEntity patientEntity = this.patientService.updatePartial(id, patientDTO);
        PatientDTO responseDTO = PatientDTO.from(patientEntity);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT') or hasRole('NURSE') or hasRole('DOCTOR')")
    @GetMapping("/appointment/{id}")
    @Operation(summary = "Get patient by id")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AppointmentListDTO.class)))
    public ResponseEntity<AppointmentListDTO> getAllAppointmentsById(@PathVariable("id") int id){
        Iterable<AppointmentEntity> appointments = this.appointmentService.findByPatientId(id);
        List<AppointmentDTO> appointmentDTOList =  new ArrayList<>();
        for (AppointmentEntity appointment: appointments){
            appointmentDTOList.add(AppointmentDTO.from(appointment));
        }
        AppointmentListDTO appointmentsList =  new AppointmentListDTO(appointmentDTOList);
        return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT') or hasRole('NURSE') or hasRole('DOCTOR')")
    @Operation(summary = "Get patient procedures by id")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PatientProceduresListDTO.class)))
    @GetMapping("/procedures/{id}")
    public ResponseEntity<PatientProceduresListDTO> getAllProceduresById(@PathVariable ("id") int id){
        Iterable<PatientProceduresEntity> patientProceduresEntities = this.patientProceduresService.findByPatientId(id);
        List<PatientProceduresDTO> patientProceduresDTOList = new ArrayList<>();
        for (PatientProceduresEntity patientProcedures : patientProceduresEntities){
            patientProceduresDTOList.add(PatientProceduresDTO.from(patientProcedures));
        }
        PatientProceduresListDTO proceduresListDTO = new PatientProceduresListDTO(patientProceduresDTOList);
        return new ResponseEntity<>(proceduresListDTO, HttpStatus.OK);
    }

}

