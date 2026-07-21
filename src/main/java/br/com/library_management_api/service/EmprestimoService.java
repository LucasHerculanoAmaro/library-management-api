package br.com.library_management_api.service;

import br.com.library_management_api.dto.request.EmprestimoRequest;
import br.com.library_management_api.dto.response.EmprestimoResponse;
import br.com.library_management_api.entity.Emprestimo;
import br.com.library_management_api.entity.Livro;
import br.com.library_management_api.entity.Usuario;
import br.com.library_management_api.enums.StatusEmprestimo;
import br.com.library_management_api.enums.StatusLivro;
import br.com.library_management_api.exception.BusinessException;
import br.com.library_management_api.exception.ResourceNotFoundException;
import br.com.library_management_api.repository.EmprestimoRepository;
import br.com.library_management_api.repository.LivroRepository;
import br.com.library_management_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             UsuarioRepository usuarioRepository,
                             LivroRepository livroRepository) {

        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    @Transactional
    public EmprestimoResponse cadastrar(EmprestimoRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Livro livro = livroRepository.findById(request.getLivroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado."));

        if (!livro.getDisponivel()) {
            throw new BusinessException("Livro indisponível para empréstimo.");
        }

        Emprestimo emprestimo = Emprestimo.builder()
                .usuario(usuario)
                .livro(livro)
                .build();

        livro.setDisponivel(false);
        livro.setStatus(StatusLivro.EMPRESTADO);

        livroRepository.save(livro);

        emprestimo = emprestimoRepository.save(emprestimo);

        return converterParaResponse(emprestimo);
    }

    public List<EmprestimoResponse> listar() {

        return emprestimoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public EmprestimoResponse buscarPorId(Long id) {

        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado."));

        return converterParaResponse(emprestimo);
    }

    @Transactional
    public EmprestimoResponse devolver(Long id) {

        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emprestimo não encontrado."));

        if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            throw  new BusinessException("Empréstimo já devolvido.");
        }

        Livro livro = emprestimo.getLivro();

        livro.setDisponivel(true);
        livro.setStatus(StatusLivro.DISPONIVEL);

        livroRepository.save(livro);

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        emprestimo = emprestimoRepository.save(emprestimo);

        return converterParaResponse(emprestimo);
    }

    /* public void excluir(Long id) {

        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado."));

        emprestimoRepository.delete(emprestimo);
    } */

    private EmprestimoResponse converterParaResponse(Emprestimo emprestimo) {

        return EmprestimoResponse.builder()
                .id(emprestimo.getId())
                .usuario(emprestimo.getUsuario().getNome())
                .livro(emprestimo.getLivro().getTitulo())
                .dataEmprestimo(emprestimo.getDataEmprestimo())
                .dataPrevistaDevolucao(emprestimo.getDataPrevistaDevolucao())
                .dataDevolucao(emprestimo.getDataDevolucao())
                .status(emprestimo.getStatus())
                .build();
    }

}