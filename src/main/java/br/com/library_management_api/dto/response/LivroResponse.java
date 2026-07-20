package br.com.library_management_api.dto.response;

import br.com.library_management_api.enums.StatusLivro;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroResponse {

    private Long id;

    private String titulo;

    private String autor;

    private String isbn;

    private StatusLivro status;

    private Boolean disponivel;
}