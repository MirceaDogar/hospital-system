package ro.hospitalmanagement.system.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ro.hospitalmanagement.system.entities.PersonEntity;
import ro.hospitalmanagement.system.model.Gender;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PersonDTO {
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

    public static PersonDTO from(PersonEntity person) {

        return PersonDTO.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .birtDate(person.getBirthDate())
                .email(person.getEmail())
                .phone(person.getPhone())
                .gender(person.getGender())
                .country(person.getAddress().getCountry())
                .city(person.getAddress().getCity())
                .street(person.getAddress().getStreet())
                .address(person.getAddress().getAddress())
                .build();
    }

    public static List<PersonDTO> from(List<PersonEntity> personEntities) {
        List<PersonDTO> result = new ArrayList<>();
        for (PersonEntity person : personEntities) {
            result.add(PersonDTO.from(person));
        }
        return result;
    }
}