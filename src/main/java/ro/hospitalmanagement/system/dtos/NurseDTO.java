package ro.hospitalmanagement.system.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ro.hospitalmanagement.system.entities.NurseEntity;
import ro.hospitalmanagement.system.model.Gender;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class NurseDTO { private Integer id;

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotNull
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
    String degreeNumber;
    @NotNull
    Integer experience;
    public static NurseDTO from(NurseEntity nurseEntity) {
        return NurseDTO.builder()
                .id(nurseEntity.getId())
                .firstName(nurseEntity.getFirstName())
                .lastName(nurseEntity.getLastName())
                .birtDate(nurseEntity.getBirthDate())
                .email(nurseEntity.getEmail())
                .phone(nurseEntity.getPhone())
                .gender(nurseEntity.getGender())
                .country(nurseEntity.getAddress().getCountry())
                .city(nurseEntity.getAddress().getCity())
                .street(nurseEntity.getAddress().getStreet())
                .address(nurseEntity.getAddress().getAddress())
                .degreeNumber(nurseEntity.getDegreeNumber())
                .experience(nurseEntity.getExperience())
                .build();
    }

    public static List<NurseDTO> from(List<NurseEntity> nurseEntities) {
        List<NurseDTO> results = new ArrayList<>();
        for (NurseEntity nurseEntity : nurseEntities) {
            results.add(NurseDTO.from(nurseEntity));
        }
        return results;
    }



}