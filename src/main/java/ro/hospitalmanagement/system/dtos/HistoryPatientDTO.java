package ro.hospitalmanagement.system.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class HistoryPatientDTO {
    @NotEmpty
    String lastName;
    @NotEmpty
    String firstName;
    String medicalHistory;

}