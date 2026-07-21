package br.com.library_management_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmprestimoRequest {

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long livroId;

}