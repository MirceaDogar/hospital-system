package ro.hospitalmanagement.system.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "prescription")
@Getter
@Setter
@ToString
public class PrescriptionEntity {
    @EmbeddedId
    private PrescriptionPK prescriptionPK;
    //   @Column(name = "id_doctor")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_doctor", referencedColumnName = "id")
    private DoctorEntity doctorId;


    @ToString.Exclude
    @ManyToOne
    @MapsId("patientEntity")
    @JoinColumn(name = "id_patient")
    private PatientEntity patientEntity;
    @ToString.Exclude
    @ManyToOne
    @MapsId("medicationEntity")
    @JoinColumn(name = "id_medication")
    private MedicationEntity medicationEntity;
}
