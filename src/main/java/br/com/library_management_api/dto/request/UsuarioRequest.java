package br.com.library_management_api.dto.request;

import br.com.library_management_api.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequest {

    @NotBlank
    @Schema(
            description = "Nome do usuário.",
            example = "Maria José"
    )
    private String nome;

    @NotBlank
    @Schema(
            description = "CPF do usuário.",
            example = "12345678901"
    )
    private String cpf;

    @Email
    @NotBlank
    @Schema(
            description = "E-mail do usuário.",
            example = "exemplo@email.com"
    )
    private String email;

    @NotBlank
    @Schema(
            description = "Senha do usuário.",
            example = "!23A56"
    )
    private String senha;

    @Schema(
            description = "Perfil do usuário.",
            allowableValues = {
                    "ADMIN",
                    "USUARIO"
            }
    )
    private Perfil perfil;
}