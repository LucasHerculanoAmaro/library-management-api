package br.com.library_management_api.repository;

import br.com.library_management_api.entity.Emprestimo;
import br.com.library_management_api.entity.Livro;
import br.com.library_management_api.entity.Usuario;
import br.com.library_management_api.enums.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByStatus(StatusEmprestimo status);

    List<Emprestimo> findByUsuarioId(Long usuarioId);

    List<Emprestimo> findByLivroId(Long livroId);

    boolean existsByUsuarioAndLivroAndStatus(Usuario usuario, Livro livro, StatusEmprestimo statusEmprestimo);
}
