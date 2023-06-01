package ro.hospitalmanagement.system.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "on_call")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OnCallEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_doctor", referencedColumnName = "id")
    private DoctorEntity doctor;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_nurse", referencedColumnName = "id")
    private NurseEntity nurse;
    @Column(name = "start_on_call")
    private LocalDateTime starOnCall;
    @Column(name = "end_on_call")
    private LocalDateTime endOnCall;

}