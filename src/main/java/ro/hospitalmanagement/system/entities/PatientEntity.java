package ro.hospitalmanagement.system.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "petient")
@PrimaryKeyJoinColumn(name = "id")
public class PatientEntity extends PersonEntity {

    @Column(name = "insurance_no")
    private String insuranceNumber;
    @Column(name = "insurance_company")
    private String insuranceCompany;
    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "history_patient",
            joinColumns = {@JoinColumn(name = "id_patient", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_history", referencedColumnName = "id")}
    )
    private Set<HistoryEntity> historyEntity = new LinkedHashSet<>();

}