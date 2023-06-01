package ro.hospitalmanagement.system.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ro.hospitalmanagement.system.entities.RoomEntity;
import ro.hospitalmanagement.system.model.RoomType;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDTO {
    private Integer id;
    private Integer number;
    private RoomType roomType;

    public static RoomDTO from(RoomEntity roomEntity){
        return RoomDTO.builder()
                .id(roomEntity.getId())
                .number(roomEntity.getRoomNumber())
                .roomType(roomEntity.getRoomType())
                .build();
    }
}