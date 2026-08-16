package br.com.library_management_api.controller;

import br.com.library_management_api.dto.request.UsuarioRequest;
import br.com.library_management_api.dto.response.UsuarioResponse;
import br.com.library_management_api.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@Tag(
        name = "Usuários",
        description = "Operações relacionadas ao gerenciamento de usuários da biblioteca."
)
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar usuário",
            description = "Realiza o cadastro de um novo usuário na biblioteca."
    )
    public UsuarioResponse cadastrar(
            @Valid @RequestBody UsuarioRequest request) {

        return usuarioService.cadastrar(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(
            summary = "Listar usuários",
            description = "Lista os usuários cadastrados utilizando paginação."
    )
    public Page<UsuarioResponse> listar(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "nome"
            )
            Pageable pageable) {

        return usuarioService.listar(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Busca um usuário através do seu identificador."
    )
    public UsuarioResponse buscarPorId(
            @PathVariable Long id) {

        return usuarioService.buscarPorId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados de um usuário existente."
    )
    public UsuarioResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {

        return usuarioService.atualizar(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/desativar")
    @Operation(
            summary = "Desativar usuário",
            description = "Desativar o status de um usuário."
    )
    public UsuarioResponse desativar(
            @PathVariable Long id) {

        return usuarioService.desativar(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/ativar")
    @Operation(
            summary = "Ativar usuário",
            description = "Ativar o status de um usuário."
    )
    public UsuarioResponse ativar(@PathVariable Long id) {
        return usuarioService.ativar(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Excluir usuário",
            description = "Remove um usuário pelo seu identificador."
    )
    public void excluir(
            @PathVariable Long id) {

        usuarioService.excluir(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/cpf/{cpf}")
    @Operation(
            summary = "Buscar usuário por CPF",
            description = "Busca um usuário utilizando seu CPF."
    )
    public UsuarioResponse buscarPorCpf(
            @PathVariable String cpf) {

        return usuarioService.buscarPorCpf(cpf);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email/{email}")
    @Operation(
            summary = "Buscar usuário por e-mail",
            description = "Busca um usuário utilizando seu endereço de e-mail."
    )
    public UsuarioResponse buscarPorEmail(
            @PathVariable String email) {

        return usuarioService.buscarPorEmail(email);
    }
}