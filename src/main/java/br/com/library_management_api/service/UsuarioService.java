package br.com.library_management_api.service;

import br.com.library_management_api.dto.request.UsuarioRequest;
import br.com.library_management_api.dto.response.UsuarioResponse;
import br.com.library_management_api.entity.Usuario;
import br.com.library_management_api.enums.Perfil;
import br.com.library_management_api.exception.DuplicateResourceException;
import br.com.library_management_api.exception.ResourceNotFoundException;
import br.com.library_management_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // CRUD
    public UsuarioResponse cadastrar(UsuarioRequest usuarioRequest) {
        if (usuarioRepository.existsByCpf(usuarioRequest.getCpf())) {
            throw new DuplicateResourceException("CPF já cadastrado");
        }

        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new DuplicateResourceException("E-mail já cadastrado.");
        }

        Usuario usuario = Usuario.builder()
                .nome(usuarioRequest.getNome())
                .cpf(usuarioRequest.getCpf())
                .email(usuarioRequest.getEmail())
                .senha(usuarioRequest.getSenha())
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

    public List<UsuarioResponse> listar() {

        return usuarioRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public UsuarioResponse buscarPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        return converterParaResponse(usuario);
    }

    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        usuario.setNome(request.getNome());
        usuario.setCpf(request.getCpf());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());

        usuario = usuarioRepository.save(usuario);

        return converterParaResponse(usuario);
    }

    public void excluir(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        usuarioRepository.delete(usuario);
    }

    // Filtros
    public UsuarioResponse buscarPorCpf(String cpf) {

        Usuario usuario = usuarioRepository.findByCpf(cpf)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado."));

        return converterParaResponse(usuario);
    }

    public UsuarioResponse buscarPorEmail(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado."));

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
