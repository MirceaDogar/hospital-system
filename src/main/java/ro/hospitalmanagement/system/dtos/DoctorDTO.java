package ro.hospitalmanagement.system.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ro.hospitalmanagement.system.entities.DoctorEntity;
import ro.hospitalmanagement.system.model.Gender;
import ro.hospitalmanagement.system.validators.NoDigits;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DoctorDTO {
    private Integer id;

    @NotEmpty
    @NoDigits(message = "Digits in firstname")
    private String firstName;
    @NotEmpty
    @NoDigits(message = "Digits in lastname")
    private String lastName;
    @NotEmpty
    @PastOrPresent
    private LocalDate birtDate;
    @NotEmpty
    @Email
    String email;
    @NotEmpty
    @Digits(integer = 10, fraction = 0)
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
    String degreeNumber;
    Integer experience;
    @NotEmpty
    String specialization;

    public static DoctorDTO from(DoctorEntity doctorEntity) {
        return DoctorDTO.builder()
                .id(doctorEntity.getId())
                .firstName(doctorEntity.getFirstName())
                .lastName(doctorEntity.getLastName())
                .birtDate(doctorEntity.getBirthDate())
                .email(doctorEntity.getEmail())
                .phone(doctorEntity.getPhone())
                .gender(doctorEntity.getGender())
                .country(doctorEntity.getAddress().getCountry())
                .city(doctorEntity.getAddress().getCity())
                .street(doctorEntity.getAddress().getStreet())
                .address(doctorEntity.getAddress().getAddress())
                .degreeNumber(doctorEntity.getDegreeNumber())
                .experience(doctorEntity.getExperience())
                .specialization(doctorEntity.getSpecialization())
                .build();
    }

    public static List<DoctorDTO> from(List<DoctorEntity> doctorEntities) {
        List<DoctorDTO> results = new ArrayList<>();
        for (DoctorEntity doctorEntity : doctorEntities) {
            results.add(DoctorDTO.from(doctorEntity));
        }
        return results;
    }

}