package br.com.library_management_api.service;

import br.com.library_management_api.dto.response.EmprestimoAtivoResponse;
import br.com.library_management_api.dto.response.LivroMaisEmprestadoResponse;
import br.com.library_management_api.repository.EmprestimoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    private final EmprestimoRepository emprestimoRepository;

    public RelatorioService(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
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
