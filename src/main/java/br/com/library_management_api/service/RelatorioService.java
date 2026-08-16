package br.com.library_management_api.service;

import br.com.library_management_api.dto.response.EmprestimoAtivoResponse;
import br.com.library_management_api.dto.response.LivroMaisEmprestadoResponse;
import br.com.library_management_api.entity.Usuario;
import br.com.library_management_api.exception.BusinessException;
import br.com.library_management_api.exception.ResourceNotFoundException;
import br.com.library_management_api.repository.EmprestimoRepository;
import br.com.library_management_api.repository.UsuarioRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;

    public RelatorioService(EmprestimoRepository emprestimoRepository, UsuarioRepository usuarioRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<LivroMaisEmprestadoResponse> livrosMaisEmprestados() {
        return emprestimoRepository.listarLivrosMaisEmprestados(
                PageRequest.of(0, 20)
        );
    }

    public List<EmprestimoAtivoResponse> emprestimosAtivos() {
        return emprestimoRepository.listarEmprestimosAtivos();
    }

}
