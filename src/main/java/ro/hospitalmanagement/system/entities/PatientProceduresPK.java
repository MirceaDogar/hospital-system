package ro.hospitalmanagement.system.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PatientProceduresPK implements Serializable {
    @Column(name = "id_patient")
    private int patient;
    @Column(name = "id_procedure")
    private int procedures;
    @Column(name = "id_doctor")
    private int doctor;
}