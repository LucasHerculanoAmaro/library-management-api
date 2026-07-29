package br.com.library_management_api.controller;

import br.com.library_management_api.dto.request.LivroRequest;
import br.com.library_management_api.dto.response.LivroResponse;
import br.com.library_management_api.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroResponse cadastrar(@RequestBody @Valid LivroRequest request) {
        return livroService.cadastrar(request);
    }

    @GetMapping
    public List<LivroResponse> listar() {
        return livroService.listar();
    }

    @GetMapping("/{id}")
    public LivroResponse buscarPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public LivroResponse atualizar(
            @PathVariable Long id,
            @RequestBody @Valid LivroRequest request) {

        return livroService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        livroService.excluir(id);
    }

    @GetMapping("/titulo")
    public ResponseEntity<List<LivroResponse>> buscarPorTitulo(
            @RequestParam String titulo) {

        return ResponseEntity.ok(livroService.buscarPorTitulo(titulo));
    }

    @GetMapping("/autor")
    public ResponseEntity<List<LivroResponse>> buscarPorAutor(
            @RequestParam String autor) {

        return ResponseEntity.ok(livroService.buscarPorAutor(autor));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<LivroResponse> buscarPorIsbn(
            @PathVariable String isbn) {

        return ResponseEntity.ok(livroService.buscarPorIsbn(isbn));
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<LivroResponse>> listarDisponiveis() {

        return ResponseEntity.ok(livroService.listarDisponiveis());
    }
}