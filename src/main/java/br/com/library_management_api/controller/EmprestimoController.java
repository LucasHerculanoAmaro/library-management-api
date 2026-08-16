package br.com.library_management_api.controller;

import br.com.library_management_api.dto.request.EmprestimoRequest;
import br.com.library_management_api.dto.response.EmprestimoResponse;
import br.com.library_management_api.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
@Tag(
        name = "Empréstimos",
        description = "Operações relacionadas ao gerenciamento de empréstimos"
)
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar empréstimo",
            description = "Realiza o cadastro de um novo empréstimo."
    )
    public EmprestimoResponse cadastrar(
            @Valid @RequestBody EmprestimoRequest request) {

        return emprestimoService.cadastrar(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/lote")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar empréstimos em lote",
            description = "Realiza o cadastro de vários empréstimos."
    )
    public List<EmprestimoResponse> cadastrarEmLote(
            @RequestBody List<@Valid EmprestimoRequest> requests) {

        return emprestimoService.cadastrarEmLote(requests);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(
            summary = "Listar empréstimos",
            description = "Lista os empréstimos cadastrados com paginação."
    )
    public Page<EmprestimoResponse> listar(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "dataEmprestimo",
                    direction = Direction.DESC
            )
            Pageable pageable) {

        return emprestimoService.listar(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar empréstimo por ID",
            description = "Busca um empréstimo pelo ID informado."
    )
    public EmprestimoResponse buscarPorId(@PathVariable Long id) {
        return emprestimoService.buscarPorId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/devolver")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Devolver livro",
            description = "Finaliza um empréstimo realizando a devolução do livro."
    )
    public EmprestimoResponse devolver(@PathVariable Long id) {
        return emprestimoService.devolver(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ativos")
    @Operation(
            summary = "Listar empréstimos ativos",
            description = "Lista todos os empréstimos que ainda não foram devolvidos."
    )
    public List<EmprestimoResponse> listarAtivos() {
        return emprestimoService.listarAtivos();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/devolvidos")
    @Operation(
            summary = "Listar empréstimos devolvidos",
            description = "Lista todos os empréstimos já devolvidos."
    )
    public List<EmprestimoResponse> listarDevolvidos() {
        return emprestimoService.listarDevolvidos();
    }

    @GetMapping("/usuario/{id}")
    @Operation(
            summary = "Buscar empréstimos por usuário",
            description = "Lista os empréstimos realizados por um usuário."
    )
    public List<EmprestimoResponse> buscarPorUsuario(
            @PathVariable Long id) {

        return emprestimoService.buscarPorUsuario(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/livro/{id}")
    @Operation(
            summary = "Buscar empréstimos por livro",
            description = "Lista os empréstimos relacionados a um livro."
    )
    public List<EmprestimoResponse> buscarPorLivro(
            @PathVariable Long id) {

        return emprestimoService.buscarPorLivro(id);
    }
}