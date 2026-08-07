package br.com.library_management_api.controller;

import br.com.library_management_api.dto.response.EmprestimoAtivoResponse;
import br.com.library_management_api.dto.response.LivroMaisEmprestadoResponse;
import br.com.library_management_api.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@Tag(
        name = "Relatórios",
        description = "Operações relacionadas à geração de relatórios da biblioteca."
)
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/livros-mais-emprestados")
    @Operation(
            summary = "Listar livros mais emprestados",
            description = "Lista os livros ordenados pela quantidade de empréstimos realizados."
    )
    public List<LivroMaisEmprestadoResponse> livrosMaisEmprestados() {
        return relatorioService.livrosMaisEmprestados();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/emprestimos-ativos")
    @Operation(
            summary = "Listar empréstimos ativos",
            description = "Lista todos os empréstimos que ainda não foram devolvidos."
    )
    public List<EmprestimoAtivoResponse> emprestimosAtivos() {
        return relatorioService.emprestimosAtivos();
    }
}