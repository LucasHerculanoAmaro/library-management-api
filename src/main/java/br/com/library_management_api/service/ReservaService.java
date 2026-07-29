package br.com.library_management_api.service;

import br.com.library_management_api.dto.request.ReservaRequest;
import br.com.library_management_api.dto.response.ReservaResponse;
import br.com.library_management_api.entity.Livro;
import br.com.library_management_api.entity.Reserva;
import br.com.library_management_api.entity.Usuario;
import br.com.library_management_api.enums.StatusEmprestimo;
import br.com.library_management_api.enums.StatusReserva;
import br.com.library_management_api.exception.BusinessException;
import br.com.library_management_api.exception.DuplicateResourceException;
import br.com.library_management_api.exception.ResourceNotFoundException;
import br.com.library_management_api.repository.EmprestimoRepository;
import br.com.library_management_api.repository.LivroRepository;
import br.com.library_management_api.repository.ReservaRepository;
import br.com.library_management_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmprestimoRepository emprestimoRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            LivroRepository livroRepository,
            UsuarioRepository usuarioRepository,
            EmprestimoRepository emprestimoRepository) {

        this.reservaRepository = reservaRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Transactional
    public ReservaResponse cadastrar(ReservaRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado."));

        Livro livro = livroRepository.findById(request.getLivroId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Livro não encontrado."));

        if (!usuario.getAtivo()) {
            throw new BusinessException(
                    "Usuário inativo não pode realizar reservas.");
        }

        if (livro.getDisponivel()) {
            throw new BusinessException(
                    "O livro está disponível para empréstimo. Não é necessário realizar uma reserva.");
        }

        if (emprestimoRepository.existsByUsuarioAndLivroAndStatus(
                usuario,
                livro,
                StatusEmprestimo.ATIVO)) {

            throw new BusinessException(
                    "Você já possui este livro emprestado.");
        }

        if (reservaRepository.existsByUsuarioAndLivroAndStatus(
                usuario,
                livro,
                StatusReserva.ATIVA)) {

            throw new DuplicateResourceException(
                    "Você já possui uma reserva ativa para este livro.");
        }

        Reserva reserva = Reserva.builder()
                .usuario(usuario)
                .livro(livro)
                .build();

        reserva = reservaRepository.save(reserva);

        return converterParaResponse(reserva);
    }

    public List<ReservaResponse> listar() {

        return reservaRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public ReservaResponse buscarPorId(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva não encontrada."));

        return converterParaResponse(reserva);
    }

    @Transactional
    public ReservaResponse cancelar(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva não encontrada."));

        if (reserva.getStatus() == StatusReserva.CANCELADA) {
            throw new BusinessException(
                    "A reserva já está cancelada.");
        }

        if (reserva.getStatus() == StatusReserva.ATENDIDA) {
            throw new BusinessException(
                    "A reserva já foi atendida e não pode ser cancelada.");
        }

        reserva.setStatus(StatusReserva.CANCELADA);

        reserva = reservaRepository.save(reserva);

        return converterParaResponse(reserva);
    }

    private ReservaResponse converterParaResponse(Reserva reserva) {

        return ReservaResponse.builder()
                .id(reserva.getId())
                .usuario(reserva.getUsuario().getNome())
                .livro(reserva.getLivro().getTitulo())
                .dataReserva(reserva.getDataReserva())
                .dataLimiteRetirada(reserva.getDataLimiteRetirada())
                .status(reserva.getStatus())
                .build();
    }

}