package br.com.library_management_api.dto.response;

import br.com.library_management_api.enums.StatusReserva;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ReservaResponse {

    private Long id;

    private String usuario;

    private String livro;

    private LocalDate dataReserva;

    private LocalDate dataLimiteRetirada;

    private StatusReserva status;

}