package br.com.library_management_api.security.service;

import br.com.library_management_api.dto.request.LoginRequest;
import br.com.library_management_api.dto.response.LoginResponse;
import br.com.library_management_api.exception.AuthenticationException;
import br.com.library_management_api.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserDetailsServiceImpl userDetailsService,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public LoginResponse autenticar(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );
        } catch (DisabledException e) {

            throw new AuthenticationException(
                    "Usuário inativo. Procure a administração."
            );

        } catch (BadCredentialsException e) {

            throw new AuthenticationException(
                    "E-mail ou senha inválidos."
            );
        }

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtService.gerarToken(userDetails);

        return LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .build();
    }
}