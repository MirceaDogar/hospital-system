package ro.hospitalmanagement.system.entities;

import lombok.*;
import ro.hospitalmanagement.system.model.Gender;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class PersonEntity {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    @OneToOne
    @JoinColumn(name = "id_adrdress", referencedColumnName = "id")
    private AddressEntity address;
    @Enumerated(EnumType.STRING)
    private Gender gender;

}