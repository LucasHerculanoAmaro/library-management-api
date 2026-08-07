package br.com.library_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroRequest {

    @NotBlank(message = "O título é obrigatório.")
    @Schema(
            description = "Título do livro",
            example = "Clean Code"
    )
    private String titulo;

    @NotBlank(message = "O autor é obrigatório.")
    @Schema(
            description = "Autor do livro",
            example = "Robert C. Martin"
    )
    private String autor;

    @NotBlank(message = "O ISBN é obrigatório.")
    @Schema(
            description = "ISBN único do livro",
            example = "9780132350884"
    )
    private String isbn;
}