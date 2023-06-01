package ro.hospitalmanagement.system.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ro.hospitalmanagement.system.entities.PatientProceduresEntity;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
public class PatientProceduresDTO {
    @NotEmpty
    private String patientFirstName;
    @NotEmpty
    private String patientLastName;
    @NotEmpty
    private String procedureName;
    @NotEmpty
    private String doctorFirstName;
    @NotEmpty
    private String doctorLastName;
    private String description;

    public static PatientProceduresDTO from(PatientProceduresEntity patientProceduresEntity){
        return PatientProceduresDTO.builder()
                .patientFirstName(patientProceduresEntity.getPatient().getFirstName())
                .patientLastName(patientProceduresEntity.getPatient().getLastName())
                .procedureName(patientProceduresEntity.getProcedures().getName())
                .doctorFirstName(patientProceduresEntity.getDoctor().getFirstName())
                .doctorLastName(patientProceduresEntity.getDoctor().getLastName())
                .description(patientProceduresEntity.getDescription())
                .build();
    }
}