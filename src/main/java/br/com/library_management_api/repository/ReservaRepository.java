package br.com.library_management_api.repository;

import br.com.library_management_api.entity.Livro;
import br.com.library_management_api.entity.Reserva;
import br.com.library_management_api.entity.Usuario;
import br.com.library_management_api.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Todas as reservas de um usuário
    List<Reserva> findByUsuarioId(Long usuarioId);

    // Todas as reservas de um livro
    List<Reserva> findByLivroId(Long livroId);

    // Reservas por status
    List<Reserva> findByStatus(StatusReserva status);

    // Todas as reservas por Data
    List<Reserva> findByLivroIdOrderByDataReservaAsc(Long livroId);

    // Primeira reserva da fila (a mais antiga)
    Optional<Reserva> findFirstByLivroAndStatusOrderByDataReservaAsc(
            Livro livro,
            StatusReserva status
    );

    Optional<Reserva> findFirstByLivroAndUsuarioAndStatus(
            Livro livro,
            Usuario usuario,
            StatusReserva status);

    // Verifica se um usuário já possui reserva ativa para o livro
    boolean existsByUsuarioAndLivroAndStatus(
            Usuario usuario,
            Livro livro,
            StatusReserva status
    );

}