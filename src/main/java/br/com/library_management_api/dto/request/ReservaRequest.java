package br.com.library_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados necessários para realizar uma reserva.")
public class ReservaRequest {

    @NotNull
    @Schema(
            description = "ID do usuário.",
            example = "1"
    )
    private Long usuarioId;

    @NotNull
    @Schema(
            description = "ID do livro.",
            example = "7"
    )
    private Long livroId;
}