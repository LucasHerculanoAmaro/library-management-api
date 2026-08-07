package br.com.library_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados necessários para realizar um empréstimo.")
public class EmprestimoRequest {

    @NotNull
    @Schema(
            description = "ID do usuário que realizará o empréstimo.",
            example = "1"
    )
    private Long usuarioId;

    @NotNull
    @Schema(
            description = "ID do livro que será emprestado.",
            example = "5"
    )
    private Long livroId;
}