package br.com.library_management_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroRequest {

    @NotBlank(message = "O título é obrigatório.")
    private String titulo;

    @NotBlank(message = "O autor é obrigatório.")
    private String autor;

    @NotBlank(message = "O ISBN é obrigatório.")
    private String isbn;
}