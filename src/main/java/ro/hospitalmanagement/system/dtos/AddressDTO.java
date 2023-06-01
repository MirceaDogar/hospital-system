package ro.hospitalmanagement.system.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ro.hospitalmanagement.system.entities.AddressEntity;
import ro.hospitalmanagement.system.validators.NoDigits;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AddressDTO {
    int id;
    @NoDigits
    @NotNull
    String country;
    @NoDigits
    @NotNull
    String city;
    @NoDigits
    @NotNull
    String street;
    String address;

    public static AddressDTO from(AddressEntity address) {
        return AddressDTO.builder()
                .id(address.getId())
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .address(address.getAddress())
                .build();
    }
}