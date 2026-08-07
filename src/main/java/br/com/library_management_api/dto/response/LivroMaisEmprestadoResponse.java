package br.com.library_management_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Relatório dos livros mais emprestados.")
public class LivroMaisEmprestadoResponse {

    @Schema(
            description = "Título do livro.",
            example = "Clean Code"
    )
    private String titulo;

    @Schema(
            description = "Autor",
            example = "Robert C. Martin"
    )
    private String autor;

    @Schema(
            description = "Quantidade de empréstimos realizados.",
            example = "18"
    )
    private Long totalEmprestimos;

}
