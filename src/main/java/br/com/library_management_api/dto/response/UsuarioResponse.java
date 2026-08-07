package br.com.library_management_api.dto.response;

import br.com.library_management_api.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        name = "UsuarioResponse",
        description = "DTO utilizado para retornar os dados de um usuário."
)
public class UsuarioResponse {

    @Schema(
            description = "Identificador único do usuário.",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nome completo do usuário.",
            example = "João da Silva"
    )
    private String nome;

    @Schema(
            description = "CPF do usuário.",
            example = "12345678901"
    )
    private String cpf;

    @Schema(
            description = "E-mail do usuário.",
            example = "joao@email.com"
    )
    private String email;

    @Schema(
            description = "Perfil de acesso do usuário.",
            example = "USUARIO",
            allowableValues = {
                    "USUARIO",
                    "ADMIN"
            }
    )
    private Perfil perfil;

    @Schema(
            description = "Indica se o usuário está ativo.",
            example = "true"
    )
    private Boolean ativo;

    @Schema(
            description = "Data e hora em que o usuário foi cadastrado.",
            example = "2026-08-03T14:30:00"
    )
    private LocalDateTime dataCadastro;
}