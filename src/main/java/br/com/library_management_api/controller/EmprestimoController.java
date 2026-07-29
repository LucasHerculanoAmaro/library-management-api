package br.com.library_management_api.controller;

import br.com.library_management_api.dto.request.EmprestimoRequest;
import br.com.library_management_api.dto.response.EmprestimoResponse;
import br.com.library_management_api.dto.response.LivroResponse;
import br.com.library_management_api.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PatchMapping("/{id}/devolver")
    @ResponseStatus(HttpStatus.OK)
    public EmprestimoResponse devolver(@PathVariable Long id) {
        return emprestimoService.devolver(id);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<EmprestimoResponse>> listarAtivos() {
        return ResponseEntity.ok(emprestimoService.listarAtivos());
    }

    @GetMapping("/devolvidos")
    public ResponseEntity<List<EmprestimoResponse>> listarDevolvidos() {
        return ResponseEntity.ok(emprestimoService.listarDevolvidos());
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<EmprestimoResponse>> buscarPorUsuario(
            @PathVariable Long id) {

        return ResponseEntity.ok(emprestimoService.buscarPorUsuario(id));
    }

    @GetMapping("/livro/{id}")
    public ResponseEntity<List<EmprestimoResponse>> buscarPorLivro(
            @PathVariable Long id) {

        return ResponseEntity.ok(emprestimoService.buscarPorLivro(id));
    }

}