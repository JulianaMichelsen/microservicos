package br.com.beautique.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FullAppointmentDTO {

    private Long id;
    private LocalDateTime dateTime;
    private Boolean appointmentsOpen;

    private CustomerDTO customer;
    private BeautyProcedureDTO beautyProcedure;
}
