package br.com.library_management_api.dto.response;

import br.com.library_management_api.enums.Perfil;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

    private Long id;

    private String nome;

    private String cpf;

    private String email;

    private Perfil perfil;

    private Boolean ativo;

    private LocalDateTime dataCadastro;
}