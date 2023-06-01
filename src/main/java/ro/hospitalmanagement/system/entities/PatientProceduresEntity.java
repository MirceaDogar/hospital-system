package ro.hospitalmanagement.system.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "patient_procedures")
@Getter
@Setter
@ToString
public class PatientProceduresEntity {
    @EmbeddedId
    private PatientProceduresPK id;

    private String description;
    @ToString.Exclude
    @ManyToOne
    @MapsId("patient")
    @JoinColumn(name = "id_patient")
    private PatientEntity patient;
    @ToString.Exclude
    @ManyToOne
    @MapsId("procedures")
    @JoinColumn(name = "id_procedure")
    private ProceduresEntity procedures;
    @ToString.Exclude
    @ManyToOne
    @MapsId("doctor")
    @JoinColumn(name = "id_doctor")
    private DoctorEntity doctor;
}