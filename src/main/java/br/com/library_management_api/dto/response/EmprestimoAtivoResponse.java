package br.com.library_management_api.dto.response;

import br.com.library_management_api.enums.StatusEmprestimo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "Relatório de empréstimos ativos.")
public class EmprestimoAtivoResponse {

    @Schema(
            description = "Título do livro.",
            example = "Spring Boot in Action"
    )
    private String titulo;

    @Schema(
            description = "Nome do usuário.",
            example = "Carlos Alberto"
    )
    private String usuario;

    @Schema(
            description = "Data do empréstimo."
    )
    private LocalDate dataEmprestimo;

    @Schema(
            description = "Data prevista para devolução."
    )
    private LocalDate dataPrevistaDevolucao;

    @Schema(
            description = "Status da devolução."
    )
    private StatusEmprestimo status;

}