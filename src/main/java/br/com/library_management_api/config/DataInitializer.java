package br.com.library_management_api.config;

import br.com.library_management_api.entity.Usuario;
import br.com.library_management_api.enums.Perfil;
import br.com.library_management_api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (usuarioRepository.count() == 0) {

            Usuario admin = Usuario.builder()
                    .nome("Administrador")
                    .cpf("00000000000")
                    .email("admin@biblioteca.com")
                    .senha(passwordEncoder.encode("123456"))
                    .perfil(Perfil.ADMIN)
                    .ativo(true)
                    .build();

            usuarioRepository.save(admin);

            System.out.println("""
                    
                    ===============================================
                    Usuário administrador criado com sucesso!
                    
                    Email : admin@biblioteca.com
                    Senha : 123456
                    ===============================================
                    
                    """);
        }
    }
}