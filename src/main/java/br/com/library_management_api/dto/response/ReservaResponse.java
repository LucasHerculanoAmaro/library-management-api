package br.com.library_management_api.dto.response;

import br.com.library_management_api.enums.StatusReserva;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados de uma reserva.")
public class ReservaResponse {

    @Schema(
            description = "ID da reserva.",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nome do usuário.",
            example = "Maria Souza"
    )
    private String usuario;

    @Schema(description = "Título do livro.", example = "Domain Driven Design")
    private String livro;

    @Schema(description = "Data da reserva.")
    private LocalDate dataReserva;

    @Schema(description = "Data limite para retirada.")
    private LocalDate dataLimiteRetirada;

    @Schema(description = "Status da reserva.")
    private StatusReserva status;
}