package br.com.library_management_api.controller;

import br.com.library_management_api.dto.request.EmprestimoRequest;
import br.com.library_management_api.dto.response.EmprestimoResponse;
import br.com.library_management_api.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmprestimoResponse cadastrar(@Valid @RequestBody EmprestimoRequest request) {
        return emprestimoService.cadastrar(request);
    }

    @GetMapping
    public List<EmprestimoResponse> listar() {
        return emprestimoService.listar();
    }

    @GetMapping("/{id}")
    public EmprestimoResponse buscarPorId(@PathVariable Long id) {
        return emprestimoService.buscarPorId(id);
    }

    /* @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        emprestimoService.excluir(id);
    } */

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        emprestimoService.devolver(id);
    }

}