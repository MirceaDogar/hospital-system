package ro.hospitalmanagement.system.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="history")
public class HistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "medical_history")
    private String medicalHistory;
    @ManyToMany (mappedBy = "historyEntity", fetch = FetchType.LAZY)
    private List<PatientEntity> patientEntities;
}