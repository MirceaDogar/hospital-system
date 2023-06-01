package ro.hospitalmanagement.system.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ro.hospitalmanagement.system.entities.ProceduresEntity;

import javax.validation.constraints.NotEmpty;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProceduresDTO {

    private int id;
    @NotEmpty
    private String name;

    private int cost;

    public static ProceduresDTO from(ProceduresEntity proceduresEntity) {
        return ProceduresDTO.builder()
                .id(proceduresEntity.getId())
                .name(proceduresEntity.getName())
                .cost(proceduresEntity.getCost())
                .build();
    }

}