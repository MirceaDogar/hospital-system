package ro.hospitalmanagement.system.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_doctor", referencedColumnName = "id")
    private DoctorEntity doctor;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_patient", referencedColumnName = "id")
    private PatientEntity patient;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_nurse", referencedColumnName = "id")
    private NurseEntity nurse;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_room", referencedColumnName = "id")
    private RoomEntity room;
    @Column(name = "start_appointment_time")
    private LocalDateTime startAppointment;
    @Column(name = "end_appointment_time")
    private LocalDateTime endAppointment;
}