package br.com.library_management_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resposta da autenticação")
public class LoginResponse {

    @Schema(
            description = "Token JWT gerado após autenticação",
            example = "eyJhbGciOiJIUzI1NiJ9..."
    )
    private String token;

    @Schema(
            description = "Tipo do token",
            example = "Bearer"
    )
    private String tipo;
}