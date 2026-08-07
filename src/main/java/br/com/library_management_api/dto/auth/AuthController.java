package br.com.library_management_api.dto.auth;

import br.com.library_management_api.dto.request.LoginRequest;
import br.com.library_management_api.dto.response.LoginResponse;
import br.com.library_management_api.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "Autenticação",
        description = "Operações de autenticação da API"
)
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Realizar login",
            description = "Autentica o usuário e retorna um token JWT."
    )
    public LoginResponse login(
            @RequestBody @Valid LoginRequest request) {

        return authenticationService.autenticar(request);
    }
}