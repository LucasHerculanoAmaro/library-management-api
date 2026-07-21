package br.com.library_management_api.service;

import br.com.library_management_api.dto.request.LivroRequest;
import br.com.library_management_api.dto.response.LivroResponse;
import br.com.library_management_api.entity.Livro;
import br.com.library_management_api.enums.StatusLivro;
import br.com.library_management_api.exception.DuplicateResourceException;
import br.com.library_management_api.exception.ResourceNotFoundException;
import br.com.library_management_api.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public LivroResponse cadastrar(LivroRequest request) {

        if (livroRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Já existe um livro cadastrado com este ISBN.");
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

    public List<LivroResponse> listar() {

        return livroRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public LivroResponse buscarPorId(Long id) {

        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado."));

        return converterParaResponse(livro);
    }

    public LivroResponse atualizar(Long id, LivroRequest request) {

        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado."));

        livro.setTitulo(request.getTitulo());
        livro.setAutor(request.getAutor());
        livro.setIsbn(request.getIsbn());

        livro = livroRepository.save(livro);

        return converterParaResponse(livro);
    }

    public void excluir(Long id) {

        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado."));

        livroRepository.delete(livro);
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