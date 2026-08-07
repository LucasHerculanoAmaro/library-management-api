package br.com.library_management_api.service;

import br.com.library_management_api.dto.request.UsuarioRequest;
import br.com.library_management_api.dto.response.UsuarioResponse;
import br.com.library_management_api.entity.Usuario;
import br.com.library_management_api.enums.Perfil;
import br.com.library_management_api.enums.StatusEmprestimo;
import br.com.library_management_api.exception.BusinessException;
import br.com.library_management_api.exception.DuplicateResourceException;
import br.com.library_management_api.exception.ResourceNotFoundException;
import br.com.library_management_api.repository.EmprestimoRepository;
import br.com.library_management_api.repository.UsuarioRepository;
import br.com.library_management_api.security.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final EmprestimoRepository emprestimoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(
            EmprestimoRepository emprestimoRepository,
            PasswordEncoder passwordEncoder,
            UsuarioRepository usuarioRepository
    ) {
        this.emprestimoRepository = emprestimoRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    // CRUD
    @Transactional
    public UsuarioResponse cadastrar(UsuarioRequest usuarioRequest) {
        if (usuarioRepository.existsByCpf(usuarioRequest.getCpf())) {
            throw new DuplicateResourceException(
                    "Já existe um usuário com o CPF " + usuarioRequest.getCpf() + ".");
        }

        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new DuplicateResourceException(
                    "Já existe um usuário com o e-mail " + usuarioRequest.getEmail() + ".");
        }

        Usuario usuario = Usuario.builder()
                .nome(usuarioRequest.getNome())
                .cpf(usuarioRequest.getCpf())
                .email(usuarioRequest.getEmail())
                //.senha(usuarioRequest.getSenha())
                .senha(passwordEncoder.encode(usuarioRequest.getSenha()))
                .perfil(
                    usuarioRequest.getPerfil() != null
                        ? usuarioRequest.getPerfil()
                        : Perfil.USUARIO
                )
                .ativo(true)
                .build();

        usuario = usuarioRepository.save(usuario);

        return converterParaResponse(usuario);
    }

    public Page<UsuarioResponse> listar(Pageable pageable) {

        return usuarioRepository.findAll(pageable)
                .map(this::converterParaResponse);
    }

    public UsuarioResponse buscarPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Usuário com ID " + id + " não encontrado."
                        )
                );

        String emailLogado = SecurityUtil.getUsuarioLogado();

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(
                                role -> role.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        if(!isAdmin &&
                !usuario.getEmail().equals(emailLogado)){

            throw new BusinessException(
                    "Você só pode acessar seus próprios dados."
            );
        }

        return converterParaResponse(usuario);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário com ID " + id + " não encontrado."));

        if (!usuario.getCpf().equals(request.getCpf())
                && usuarioRepository.existsByCpf(request.getCpf())) {

            throw new DuplicateResourceException(
                    "Já existe um usuário com o CPF " + request.getCpf() + ".");
        }

        if (!usuario.getEmail().equals(request.getEmail())
                && usuarioRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Já existe um usuário com o e-mail " + request.getEmail() + ".");
        }

        usuario.setNome(request.getNome());
        usuario.setCpf(request.getCpf());
        usuario.setEmail(request.getEmail());
        //usuario.setSenha(request.getSenha());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));

        usuario = usuarioRepository.save(usuario);

        return converterParaResponse(usuario);
    }

    @Transactional
    public UsuarioResponse desativar(Long id){

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário com ID " + id + " não encontrado."));

        usuario.setAtivo(false);

        return converterParaResponse(
                usuarioRepository.save(usuario)
        );
    }

    @Transactional
    public void excluir(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário com ID " + id + " não encontrado."));

        boolean possuiEmprestimoAtivo =
                emprestimoRepository.existsByUsuarioAndStatus(
                        usuario,
                        StatusEmprestimo.ATIVO);

        if (possuiEmprestimoAtivo) {

            throw new BusinessException(
                    "Não é possível excluir um usuário com empréstimos ativos.");
        }

        usuarioRepository.delete(usuario);
    }

    // Filtros
    public UsuarioResponse buscarPorCpf(String cpf) {

        Usuario usuario = usuarioRepository.findByCpf(cpf)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário com o CPF " + cpf + " não encontrado."));

        return converterParaResponse(usuario);
    }

    public UsuarioResponse buscarPorEmail(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário com o e-mail " + email + " não encontrado."));

        return converterParaResponse(usuario);
    }

    private UsuarioResponse converterParaResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .cpf(usuario.getCpf())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil())
                .ativo(usuario.getAtivo())
                .dataCadastro(usuario.getDataCadastro())
                .build();
    }

}
