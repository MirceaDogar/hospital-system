package ro.hospitalmanagement.system.entities;

import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor")
@PrimaryKeyJoinColumn(name = "id")
public class DoctorEntity extends PersonEntity{

    private String degreeNumber;
    private int experience;
    private String specialization;

}