package ro.hospitalmanagement.system.entities;

import lombok.*;
import javax.persistence.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nurse")
@PrimaryKeyJoinColumn(name = "id")
public class NurseEntity extends PersonEntity{
    //      @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    @Column(name = "degree_number")
    private String degreeNumber;
    @Column(name = "exerience")
    private int experience;
//    @ToString.Exclude
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "id_person", referencedColumnName = "id")
//    private PersonEntity personEntity;
}
