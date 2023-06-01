package ro.hospitalmanagement.system.requests;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ro.hospitalmanagement.system.entities.UserEntity;
import ro.hospitalmanagement.system.validators.Password;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SignupRequest {
    private int id;
    private String firstName;

    private String lastName;

    private String username;
    @Password
    private String password;

    private int age;
    private boolean active;
    Set<String> role;

    public static SignupRequest from(UserEntity userEntity) {
        return SignupRequest.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .age(userEntity.getAge())
                .build();
    }

}