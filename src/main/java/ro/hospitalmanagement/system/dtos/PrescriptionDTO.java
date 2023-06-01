package ro.hospitalmanagement.system.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ro.hospitalmanagement.system.entities.PrescriptionEntity;


@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionDTO {


    private String patientLastname;
    private String patientFirstname;
    private String medicationName;
    private String doctorLastname;
    private String doctorFirstname;

    public static PrescriptionDTO from(PrescriptionEntity prescriptionEntity) {
        return PrescriptionDTO.builder()
                .patientLastname(prescriptionEntity.getPatientEntity().getLastName())
                .patientFirstname(prescriptionEntity.getPatientEntity().getFirstName())
                .medicationName(prescriptionEntity.getMedicationEntity().getName())
                .doctorLastname(prescriptionEntity.getDoctorId().getLastName())
                .doctorFirstname(prescriptionEntity.getDoctorId().getLastName())
                .build();
    }
}