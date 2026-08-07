package br.com.library_management_api.controller;

import br.com.library_management_api.dto.request.ReservaRequest;
import br.com.library_management_api.dto.response.ReservaResponse;
import br.com.library_management_api.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@Tag(
        name = "Reservas",
        description = "Operações relacionadas ao gerenciamento de reservas de livros."
)
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar reserva",
            description = "Realiza o cadastro de uma nova reserva de livro."
    )
    public ReservaResponse cadastrar(
            @RequestBody @Valid ReservaRequest request) {

        return reservaService.cadastrar(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
    @GetMapping
    @Operation(
            summary = "Listar reservas",
            description = "Lista as reservas cadastradas com paginação."
    )
    public Page<ReservaResponse> listar(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "dataReserva"
            )
            Pageable pageable) {

        return reservaService.listar(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar reserva por ID",
            description = "Busca uma reserva pelo ID informado."
    )
    public ReservaResponse buscarPorId(
            @PathVariable Long id) {

        return reservaService.buscarPorId(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
    @PatchMapping("/{id}/cancelar")
    @Operation(
            summary = "Cancelar reserva",
            description = "Realiza o cancelamento de uma reserva existente."
    )
    public ReservaResponse cancelar(
            @PathVariable Long id) {

        return reservaService.cancelar(id);
    }
}