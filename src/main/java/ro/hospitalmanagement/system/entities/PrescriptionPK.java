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
public class PrescriptionPK implements Serializable {
    @Column(name = "id_patient")
    private int patientEntity;
    @Column(name = "id_medication")
    private int medicationEntity;
}