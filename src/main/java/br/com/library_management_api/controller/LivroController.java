package br.com.library_management_api.controller;

import br.com.library_management_api.dto.request.LivroRequest;
import br.com.library_management_api.dto.response.LivroResponse;
import br.com.library_management_api.service.LivroService;
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

import java.util.List;

@RestController
@RequestMapping("/api/livros")
@Tag(
        name = "Livros",
        description = "Operações relacionadas ao gerenciamento de livros"
)
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar livro",
            description = "Realiza o cadastro de um novo livro."
    )
    public LivroResponse cadastrar(@RequestBody @Valid LivroRequest request) {
        return livroService.cadastrar(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/lote")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar livros em lote",
            description = "Realiza o cadastro de vários livros."
    )
    public List<LivroResponse> cadastrarEmLote(
            @RequestBody List< @Valid LivroRequest> requests) {

        return livroService.cadastrarEmLote(requests);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @GetMapping
    @Operation(
            summary = "listar livros",
            description = "Lista os livros cadastrados com paginação."
    )
    public Page<LivroResponse> listar(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "titulo"
            )
            Pageable pageable) {
        return livroService.listar(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar livro por ID",
            description = "Realiza a busca de um livro pelo ID."
    )
    public LivroResponse buscarPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar livro",
            description = "Atualiza um livro a partir do ID informado."
    )
    public LivroResponse atualizar(
            @PathVariable Long id,
            @RequestBody @Valid LivroRequest request) {

        return livroService.atualizar(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Excluir livro",
            description = "Excluir um livro pelo ID."
    )
    public void excluir(@PathVariable Long id) {
        livroService.excluir(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
    @GetMapping("/titulo")
    @Operation(
            summary = "Buscar livro pelo título",
            description = "Buscar livro pelo título informado."
    )
    public List<LivroResponse> buscarPorTitulo(
            @RequestParam String titulo) {

        return livroService.buscarPorTitulo(titulo);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
    @GetMapping("/autor")
    @Operation(
            summary = "Buscar livro pelo autor",
            description = "Buscar livro pelo Autor informado."
    )
    public List<LivroResponse> buscarPorAutor(
            @RequestParam String autor) {

        return livroService.buscarPorAutor(autor);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
    @GetMapping("/isbn/{isbn}")
    @Operation(
            summary = "Buscar livro pelo ISBN",
            description = "Buscar livro pelo  ISBN informado."
    )
    public LivroResponse buscarPorIsbn(
            @PathVariable String isbn) {

        return livroService.buscarPorIsbn(isbn);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
    @GetMapping("/disponiveis")
    @Operation(
            summary = "Buscar livros disponíveis",
            description = "Lista todos os livros disponíveis para empréstimo."
    )
    public List<LivroResponse> listarDisponiveis() {

        return livroService.listarDisponiveis();
    }
}