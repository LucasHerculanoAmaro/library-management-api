package br.com.library_management_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaRequest {

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long livroId;

}