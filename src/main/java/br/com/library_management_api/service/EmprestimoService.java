package br.com.library_management_api.service;

import br.com.library_management_api.dto.request.EmprestimoRequest;
import br.com.library_management_api.dto.response.EmprestimoResponse;
import br.com.library_management_api.entity.Emprestimo;
import br.com.library_management_api.entity.Livro;
import br.com.library_management_api.entity.Reserva;
import br.com.library_management_api.entity.Usuario;
import br.com.library_management_api.enums.StatusEmprestimo;
import br.com.library_management_api.enums.StatusLivro;
import br.com.library_management_api.enums.StatusReserva;
import br.com.library_management_api.exception.BusinessException;
import br.com.library_management_api.exception.ResourceNotFoundException;
import br.com.library_management_api.repository.EmprestimoRepository;
import br.com.library_management_api.repository.LivroRepository;
import br.com.library_management_api.repository.ReservaRepository;
import br.com.library_management_api.repository.UsuarioRepository;
import br.com.library_management_api.security.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final ReservaRepository reservaRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             UsuarioRepository usuarioRepository,
                             LivroRepository livroRepository,
                             ReservaRepository reservaRepository) {

        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
        this.reservaRepository = reservaRepository;
    }

    // CRUD
    @Transactional
    public EmprestimoResponse cadastrar(EmprestimoRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Usuário com ID " + request.getUsuarioId() + " não encontrado."));

        if (!usuario.getAtivo()) {
            throw new BusinessException(
                    "Usuário inativo. Não é possível realizar empréstimo."
            );
        }

        Livro livro = livroRepository.findById(request.getLivroId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Livro com ID " + request.getLivroId() + " não encontrado."));

        if (!livro.getDisponivel()
                && livro.getStatus() != StatusLivro.RESERVADO) {

            throw new BusinessException("Livro indisponível para empréstimo.");
        }

        Optional<Reserva> reserva = reservaRepository
                .findFirstByLivroAndUsuarioAndStatus(
                        livro,
                        usuario,
                        StatusReserva.DISPONIVEL_PARA_RETIRADA
                );

        Optional<Reserva> reservaAtiva = reservaRepository
                .findFirstByLivroAndStatusOrderByDataReservaAsc(
                        livro,
                        StatusReserva.DISPONIVEL_PARA_RETIRADA);

        if (reservaAtiva.isPresent()
                && !reservaAtiva.get().getUsuario().getId().equals(usuario.getId())) {

            throw new BusinessException(
                    "Este livro está reservado para outro usuário.");
        }

        reserva.ifPresent(r -> {
            r.setStatus(StatusReserva.ATENDIDA);
            reservaRepository.save(r);
        });

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

    @Transactional
    public List<EmprestimoResponse> cadastrarEmLote(
            List<EmprestimoRequest> requests
    ) {

        return requests.stream()
                .map(this::cadastrar)
                .toList();
    }

    public Page<EmprestimoResponse> listar(Pageable pageable) {

        return emprestimoRepository.findAll(pageable)
                .map(this::converterParaResponse);
    }

    public EmprestimoResponse buscarPorId(Long id) {

        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Emprestimo com ID " + id + " não encontrado."
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

        if (!isAdmin &&
                !emprestimo.getUsuario()
                        .getEmail()
                        .equals(emailLogado)) {

            throw new BusinessException(
                    "Você só pode acessar seus próprios empréstimos."
            );
        }

        return converterParaResponse(emprestimo);
    }

    @Transactional
    public EmprestimoResponse devolver(Long id) {

        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Empréstimo com ID " + id + " não encontrado."));

        if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            throw  new BusinessException(
                    "Empréstimo já devolvido.");
        }

        Livro livro = emprestimo.getLivro();

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        Optional<Reserva> reserva = reservaRepository
                .findFirstByLivroAndStatusOrderByDataReservaAsc(
                        livro,
                        StatusReserva.ATIVA
                );

        if (reserva.isPresent()) {

            Reserva r = reserva.get();

            r.setStatus(StatusReserva.DISPONIVEL_PARA_RETIRADA);
            r.setDataLimiteRetirada(LocalDate.now().plusDays(2));

            livro.setDisponivel(false);
            livro.setStatus(StatusLivro.RESERVADO);

            reservaRepository.save(r);

        } else {

            livro.setDisponivel(true);
            livro.setStatus(StatusLivro.DISPONIVEL);

        }

        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        return converterParaResponse(emprestimo);
    }

    /* public void excluir(Long id) {

        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado."));

        emprestimoRepository.delete(emprestimo);
    } */

    // Filtros
    public List<EmprestimoResponse> listarAtivos() {

        return emprestimoRepository.findByStatus(StatusEmprestimo.ATIVO)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public List<EmprestimoResponse> listarDevolvidos() {

        return emprestimoRepository.findByStatus(StatusEmprestimo.DEVOLVIDO)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public List<EmprestimoResponse> buscarPorUsuario(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário com ID " + usuarioId + " não encontrado."
                        ));

        String emailLogado = SecurityUtil.getUsuarioLogado();

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isAdmin =
                authentication
                        .getAuthorities()
                        .stream()
                        .anyMatch(role -> role
                                .getAuthority()
                                .equals("ROLE_ADMIN")
                );

        if (!isAdmin &&
                !usuario
                        .getEmail()
                        .equals(emailLogado)
        ) {
            throw new BusinessException(
                    "Você só pode acessar seus próprios empréstimos."
            );
        }

        return emprestimoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public List<EmprestimoResponse> buscarPorLivro(Long livroId) {

        return emprestimoRepository.findByLivroId(livroId)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

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