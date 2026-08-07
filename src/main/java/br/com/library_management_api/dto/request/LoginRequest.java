package br.com.library_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados utilizados para autenticação do usuário")
public class LoginRequest {

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Schema(
            description = "E-mail do usuário",
            example = "admin@biblioteca.com"
    )
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Schema(
            description = "Senha do usuário",
            example = "123456"
    )
    private String senha;

}