package ro.hospitalmanagement.system.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.hospitalmanagement.system.model.RoomType;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "room")
public class RoomEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Column(name = "number", unique = true)
    private int roomNumber;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private boolean available;
}