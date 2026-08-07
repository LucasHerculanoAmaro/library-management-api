package br.com.library_management_api.service;

import br.com.library_management_api.dto.request.LivroRequest;
import br.com.library_management_api.dto.response.LivroResponse;
import br.com.library_management_api.entity.Livro;
import br.com.library_management_api.enums.StatusEmprestimo;
import br.com.library_management_api.enums.StatusLivro;
import br.com.library_management_api.exception.BusinessException;
import br.com.library_management_api.exception.DuplicateResourceException;
import br.com.library_management_api.exception.ResourceNotFoundException;
import br.com.library_management_api.repository.EmprestimoRepository;
import br.com.library_management_api.repository.LivroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;

    public LivroService(LivroRepository livroRepository, EmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    // CRUD
    public LivroResponse cadastrar(LivroRequest request) {

        if (livroRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Já existe um livro com o ISBN " + request.getIsbn() + ".");
        }

        Livro livro = Livro.builder()
                .titulo(request.getTitulo())
                .autor(request.getAutor())
                .isbn(request.getIsbn())
                .status(StatusLivro.DISPONIVEL)
                .disponivel(true)
                .build();

        livro = livroRepository.save(livro);

        return converterParaResponse(livro);
    }

    @Transactional
    public List<LivroResponse> cadastrarEmLote(List<LivroRequest> request) {
        return request.stream()
                .map(this::cadastrar)
                .toList();
    }

    public Page<LivroResponse> listar(Pageable pageable) {

        return livroRepository.findAll(pageable)
                .map(this::converterParaResponse);
    }

    public LivroResponse buscarPorId(Long id) {

        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro com id: " + id + " não encontrado."));

        return converterParaResponse(livro);
    }

    @Transactional
    public LivroResponse atualizar(Long id, LivroRequest request) {

        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro com id: " + id + " não encontrado."));

        if (!livro.getIsbn().equals(request.getIsbn())
                && livroRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException(
                    "Já existe um livro com o ISBN " + request.getIsbn() + "."
            );
        }

        livro.setTitulo(request.getTitulo());
        livro.setAutor(request.getAutor());
        livro.setIsbn(request.getIsbn());

        livro = livroRepository.save(livro);

        return converterParaResponse(livro);
    }

    @Transactional
    public void excluir(Long id) {

        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro com id: " + id + " não encontrado."));

        boolean possuiEmprestimoAtivo = emprestimoRepository.existsByLivroAndStatus(
                livro,
                StatusEmprestimo.ATIVO
        );

        if (possuiEmprestimoAtivo) {
            throw new BusinessException(
                    "Não é possível excluir um livro com empréstimo ativo"
            );
        }

        livroRepository.delete(livro);
    }

    // Filtros
    public List<LivroResponse> buscarPorTitulo(String titulo) {

        return livroRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public List<LivroResponse> buscarPorAutor(String autor) {

        return livroRepository.findByAutorContainingIgnoreCase(autor)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public LivroResponse buscarPorIsbn(String isbn) {

        Livro livro = livroRepository.findByIsbn(isbn)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Livro com ISBN " + isbn + "não encontrado."));

        return converterParaResponse(livro);
    }

    public List<LivroResponse> listarDisponiveis() {

        return livroRepository.findByDisponivelTrue()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    private LivroResponse converterParaResponse(Livro livro) {

        return LivroResponse.builder()
                .id(livro.getId())
                .titulo(livro.getTitulo())
                .autor(livro.getAutor())
                .isbn(livro.getIsbn())
                .status(livro.getStatus())
                .disponivel(livro.getDisponivel())
                .build();
    }
}