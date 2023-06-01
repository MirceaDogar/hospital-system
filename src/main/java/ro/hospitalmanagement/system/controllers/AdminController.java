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
import ro.hospitalmanagement.system.services.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
@Validated
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private DoctorService doctorService;
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
    private PrescriptionService prescriptionService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PersonService personService;
    @Autowired
    private AddressService addressService;

    @Autowired
    private OnCallService onCallService;
    @Autowired
    private PatientProceduresService patientProceduresService;
    @Autowired
    private EmailService emailService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create admin")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = AdminEntity.class)))
    @PostMapping("/admin")
    public ResponseEntity<AdminEntity> create(@Valid @RequestBody AdminEntity admin) throws MessagingException {
        log.debug("Create admin");
        this.emailService.sendEmail(admin.getEmail(), "Confirmation email", "You are not in database, please sing-up");
        AdminEntity adminEntity = this.adminService.add(admin);
        return new ResponseEntity<>(adminEntity, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create person")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = PersonDTO.class)))
    @PostMapping("/person")
    public ResponseEntity<PersonDTO> create(@Valid @RequestBody PersonDTO person) {
        log.debug("Create person");
        PersonEntity personEntity = this.personService.add(person);
        return new ResponseEntity<>(PersonDTO.from(personEntity), HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create doctor")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = DoctorDTO.class)))
    @PostMapping("/doctor")
    public ResponseEntity<DoctorDTO> create(@Valid @RequestBody DoctorDTO doctorDTO) throws AlreadyExistsException {
        log.debug("Create doctor");
        DoctorEntity doctorEntity = this.doctorService.add(doctorDTO);
        return new ResponseEntity<>(DoctorDTO.from(doctorEntity), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create nurse")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = NurseDTO.class)))
    @PostMapping("/nurse")
    public ResponseEntity<NurseDTO> create(@Valid @RequestBody NurseDTO nurseDTO) throws AlreadyExistsException {
        log.debug("Create nurse");
        NurseEntity nurseEntity = this.nurseService.add(nurseDTO);
        return new ResponseEntity<>(NurseDTO.from(nurseEntity), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create address")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = AddressDTO.class)))
    @PostMapping("/address")
    public ResponseEntity<AddressDTO> create(@Valid @RequestBody AddressDTO addressDTO) {
        log.debug("Create address");
        AddressEntity addressEntity = this.addressService.add(addressDTO);
        return new ResponseEntity<>(addressDTO.from(addressEntity), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create on call")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = OnCallDTO.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("/oncall")
    public ResponseEntity<OnCallDTO> create(@Valid @RequestBody OnCallDTO onCallDTO) throws MessagingException, InexistentResourceException {
        log.debug("Create on call");
        OnCallEntity onCallEntity = this.onCallService.add(onCallDTO);
        return new ResponseEntity<>(onCallDTO.from(onCallEntity), HttpStatus.CREATED);

    }


    //GET - ALL
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all patients")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PatientListDTO.class)))
    @GetMapping("/patient")
    public ResponseEntity<PatientListDTO> getAllPatient() {
        Iterable<PatientEntity> patients = this.patientService.findAll();
        List<PatientDTO> patientDTOList = new ArrayList<>();
        for (PatientEntity patientEntity : patients) {
            patientDTOList.add(PatientDTO.from(patientEntity));
        }
        PatientListDTO patientListDTO = new PatientListDTO(patientDTOList);
        return new ResponseEntity<>(patientListDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all addresses")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AddressListDTO.class)))
    @GetMapping("/addresses")
    public ResponseEntity<AddressListDTO> getAllAddress() {
        Iterable<AddressEntity> addresses = this.addressService.findAll();
        List<AddressDTO> addressDTOList = new ArrayList<>();
        for (AddressEntity addressEntity : addresses) {
            addressDTOList.add(AddressDTO.from(addressEntity));
        }
        AddressListDTO addressListDTO = new AddressListDTO(addressDTOList);
        return new ResponseEntity<>(addressListDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all appointments")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AppointmentListDTO.class)))
    @GetMapping("/appointments")
    public ResponseEntity<AppointmentListDTO> getAllAppointment() {
        Iterable<AppointmentEntity> appointments = this.appointmentService.findAll();
        List<AppointmentDTO> appointmentDTOList = new ArrayList<>();
        for (AppointmentEntity appointmentEntity : appointments) {
            appointmentDTOList.add(AppointmentDTO.from(appointmentEntity));
        }
        AppointmentListDTO appointmentListDTO = new AppointmentListDTO(appointmentDTOList);
        return new ResponseEntity<>(appointmentListDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get history")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = HistoryListDTO.class)))
    @GetMapping("/history")
    public ResponseEntity<HistoryListDTO> getAllHistory() {
        Iterable<HistoryEntity> history = this.historyService.findAll();
        List<HistoryDTO> historyDTOList = new ArrayList<>();
        for (HistoryEntity historyEntity : history) {
            historyDTOList.add(HistoryDTO.from(historyEntity));
        }
        HistoryListDTO historyListDTO = new HistoryListDTO(historyDTOList);
        return new ResponseEntity<>(historyListDTO, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all nurses")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = NurseListDTO.class)))
    @GetMapping("/nurse")
    public ResponseEntity<NurseListDTO> getAllNurse() {
        Iterable<NurseEntity> nurses = this.nurseService.findAll();
        List<NurseDTO> nurseDTOList = new ArrayList<>();
        for (NurseEntity nurseEntity : nurses) {
            nurseDTOList.add(NurseDTO.from(nurseEntity));
        }
        NurseListDTO nurseListDTO = new NurseListDTO(nurseDTOList);
        return new ResponseEntity<>(nurseListDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all on calls")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = OnCallListDTO.class)))
    @GetMapping("/oncall")
    public ResponseEntity<OnCallListDTO> getAllOnCall() {
        Iterable<OnCallEntity> onCalls = this.onCallService.findAll();
        List<OnCallDTO> onCallDTOList = new ArrayList<>();
        for (OnCallEntity onCallEntity : onCalls) {
            onCallDTOList.add(OnCallDTO.from(onCallEntity));
        }
        OnCallListDTO onCallListDTO = new OnCallListDTO(onCallDTOList);
        return new ResponseEntity<>(onCallListDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all patient procedures")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PatientProceduresListDTO.class)))
    @GetMapping("/patientprocedures")
    public ResponseEntity<PatientProceduresListDTO> getAllPatientProcedures() {
        Iterable<PatientProceduresEntity> patientProcedures = this.patientProceduresService.findAll();
        List<PatientProceduresDTO> patientProceduresDTOList = new ArrayList<>();
        for (PatientProceduresEntity patientProceduresEntity : patientProcedures) {
            patientProceduresDTOList.add(PatientProceduresDTO.from(patientProceduresEntity));
        }
        PatientProceduresListDTO patientProceduresListDTO = new PatientProceduresListDTO(patientProceduresDTOList);
        return new ResponseEntity<>(patientProceduresListDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all persons")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PersonListDTO.class)))
    @GetMapping("/person")
    public ResponseEntity<PersonListDTO> getAllPerson() {
        Iterable<PersonEntity> personProcedures = this.personService.findAll();
        List<PersonDTO> personDTOList = new ArrayList<>();
        for (PersonEntity personEntity : personProcedures) {
            personDTOList.add(PersonDTO.from(personEntity));
        }
        PersonListDTO personListDTO = new PersonListDTO(personDTOList);
        return new ResponseEntity<>(personListDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all prescriptions")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PrescriptionListDTO.class)))
    @GetMapping("/prescription")
    public ResponseEntity<PrescriptionListDTO> getAllPrescription() {
        Iterable<PrescriptionEntity> prescriptions = this.prescriptionService.findAll();
        List<PrescriptionDTO> prescriptionDTOList = new ArrayList<>();
        for (PrescriptionEntity prescriptionEntity : prescriptions) {
            prescriptionDTOList.add(PrescriptionDTO.from(prescriptionEntity));
        }
        PrescriptionListDTO prescriptionListDTO = new PrescriptionListDTO(prescriptionDTOList);
        return new ResponseEntity<>(prescriptionListDTO, HttpStatus.OK);
    }
    @Operation(summary = "Search doctor by experience and specialization")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DoctorListDTO.class)))
    @GetMapping("/searchDocByExpAndSpec")
    public ResponseEntity<DoctorListDTO> searchDoctor(@RequestParam(value = "experience", required = false) int experience,
                                                      @RequestParam(value = "specialization", required = false) String specialization) {
        List<DoctorEntity> entityList = this.doctorService.searchByExperienceAndSpecialization(experience, specialization);
        List<DoctorDTO> doctorDTOList = DoctorDTO.from(entityList);
        return new ResponseEntity<>(new DoctorListDTO(doctorDTOList), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all procedures")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProceduresDTO.class)))
    @GetMapping("/procedures")
    public ResponseEntity<ProceduresListDTO> getAllProcedures() {
        Iterable<ProceduresEntity> procedures = this.proceduresService.findAll();
        List<ProceduresDTO> proceduresDTOList = new ArrayList<>();
        for (ProceduresEntity proceduresEntity : procedures) {
            proceduresDTOList.add(ProceduresDTO.from(proceduresEntity));
        }
        ProceduresListDTO proceduresListDTO = new ProceduresListDTO(proceduresDTOList);
        return new ResponseEntity<>(proceduresListDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all rooms")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = RoomDTO.class)))
    @GetMapping("/room")
    public ResponseEntity<RoomListDTO> getAllRoom() {
        Iterable<RoomEntity> rooms = this.roomService.findAll();
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for (RoomEntity roomEntity : rooms) {
            roomDTOList.add(RoomDTO.from(roomEntity));
        }
        RoomListDTO roomListDTO = new RoomListDTO(roomDTOList);
        return new ResponseEntity<>(roomListDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get person by ID")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PersonDTO.class)))
    @GetMapping("/person/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@Min(1) @PathVariable("id") Integer id) throws InexistentResourceException {
        PersonEntity personEntity = this.personService.findById(id);
        PersonDTO personDTO = PersonDTO.from(personEntity);
        return new ResponseEntity<>(personDTO, HttpStatus.OK);
    }

    //DELETE - ALL
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete doctor by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("doctor/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable("id") int id) {
        log.info("Delete doctor");
        try {
            this.doctorService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete address by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("address/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("id") int id) {
        log.info("Delete address");
        try {
            this.addressService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete admin by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("admin/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable("id") int id) {
        log.info("Delete admin");
        try {
            this.adminService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete history by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("history/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable("id") int id) {
        log.info("Delete history");
        try {
            this.historyService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete medication by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("medication/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable("id") int id) {
        log.info("Delete medication");
        try {
            this.medicationService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete nurse by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("nurse/{id}")
    public ResponseEntity<Void> deleteNurse(@PathVariable("id") int id) {
        log.info("Delete nurse");
        try {
            this.nurseService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete on call by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("oncall/{id}")
    public ResponseEntity<Void> deleteOnCall(@PathVariable("id") int id) {
        log.info("Delete on call");
        try {
            this.onCallService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete patient by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("patient/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") int id) {
        log.info("Delete patient");
        try {
            this.patientService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete person by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("person/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") int id) {
        log.info("Delete person");
        try {
            this.personService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("prescription/{id}/{id2}/")
    public ResponseEntity<Void> deletePrescription(@PathVariable("id") int id,@PathVariable ("id2")int id2) {
        log.info("Delete prescription");
        try {
            this.prescriptionService.delete(id,id2);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InexistentResourceException e) {
            log.warn("Inexistent resource exception {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete procedure by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("procedures/{id}")
    public ResponseEntity<Void> deleteProcedures(@PathVariable("id") int id) throws InexistentResourceException {
        log.info("Delete procedures");
        this.proceduresService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete room by ID")
    @ApiResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("room/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("id") int id) throws InexistentResourceException {
        log.info("Delete room");
        this.roomService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all doctors")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DoctorListDTO.class)))
    @GetMapping("/doctors")
    public ResponseEntity<DoctorListDTO> getAllDoctor() {
        Iterable<DoctorEntity> doctors = this.doctorService.findAll();
        List<DoctorDTO> doctorDTOList = new ArrayList<>();
        for (DoctorEntity doctorEntity : doctors) {
            doctorDTOList.add(DoctorDTO.from(doctorEntity));
        }
        DoctorListDTO doctorListDTO = new DoctorListDTO(doctorDTOList);
        return new ResponseEntity<>(doctorListDTO, HttpStatus.OK);
    }
}
