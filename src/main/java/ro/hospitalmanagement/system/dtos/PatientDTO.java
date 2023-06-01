package ro.hospitalmanagement.system.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ro.hospitalmanagement.system.entities.PatientEntity;
import ro.hospitalmanagement.system.model.Gender;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDTO {
    private Integer id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    private LocalDate birtDate;
    @NotEmpty
    @Email
    String email;
    @NotEmpty
    String phone;

    Gender gender;
    @NotEmpty
    String country;
    @NotEmpty
    String city;
    @NotEmpty
    String street;
    @NotEmpty
    String address;
    @NotEmpty
    String insuranceNumber;
    @NotEmpty
    String insuranceCompany;

    LocalDate enrollmentDate;


    public static PatientDTO from(PatientEntity patientEntity){
        return PatientDTO.builder()
                .id(patientEntity.getId())
                .firstName(patientEntity.getFirstName())
                .lastName(patientEntity.getLastName())
                .birtDate(patientEntity.getBirthDate())
                .email(patientEntity.getEmail())
                .phone(patientEntity.getPhone())
                .gender(patientEntity.getGender())
                .country(patientEntity.getAddress().getCountry())
                .city(patientEntity.getAddress().getCity())
                .street(patientEntity.getAddress().getStreet())
                .address(patientEntity.getAddress().getAddress())
                .insuranceNumber(patientEntity.getInsuranceNumber())
                .insuranceCompany(patientEntity.getInsuranceCompany())
                .enrollmentDate(patientEntity.getEnrollmentDate()).build();
    }

    public static List<PatientDTO> from (List<PatientEntity> patientEntities){
        List<PatientDTO> results = new ArrayList<>();
        for (PatientEntity patientEntity : patientEntities){
            results.add(PatientDTO.from(patientEntity));
        }
        return results;
    }
}