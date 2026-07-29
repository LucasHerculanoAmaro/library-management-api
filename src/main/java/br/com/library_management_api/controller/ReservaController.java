package br.com.library_management_api.controller;

import br.com.library_management_api.dto.request.ReservaRequest;
import br.com.library_management_api.dto.response.ReservaResponse;
import br.com.library_management_api.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaResponse cadastrar(@RequestBody @Valid ReservaRequest request) {
        return reservaService.cadastrar(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservaResponse> listar() {
        return reservaService.listar();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservaResponse buscarPorId(@PathVariable Long id) {
        return reservaService.buscarPorId(id);
    }

    @PatchMapping("/{id}/cancelar")
    @ResponseStatus(HttpStatus.OK)
    public ReservaResponse cancelar(@PathVariable Long id) {
        return reservaService.cancelar(id);
    }
}