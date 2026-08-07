package br.com.library_management_api.dto.response;

import br.com.library_management_api.enums.StatusLivro;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroResponse {

    @Schema(
            description = "Identificador do livro",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Título do livro",
            example = "Clean Code"
    )
    private String titulo;

    @Schema(
            description = "Autor",
            example = "Robert C. Martin"
    )
    private String autor;

    @Schema(
            description = "ISBN",
            example = "9780132350884"
    )
    private String isbn;

    @Schema(
            description = "Status atual do livro",
            allowableValues = {
                    "DISPONIVEL",
                    "EMPRESTADO",
                    "RESERVADO"
            }
    )
    private StatusLivro status;

    @Schema(
            description = "Indica se o livro está disponível para empréstimo",
            example = "true"
    )
    private Boolean disponivel;
}