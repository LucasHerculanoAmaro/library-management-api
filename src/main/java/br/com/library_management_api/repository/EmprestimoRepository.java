package br.com.library_management_api.repository;

import br.com.library_management_api.dto.response.EmprestimoAtivoResponse;
import br.com.library_management_api.dto.response.LivroMaisEmprestadoResponse;
import br.com.library_management_api.entity.Emprestimo;
import br.com.library_management_api.entity.Livro;
import br.com.library_management_api.entity.Usuario;
import br.com.library_management_api.enums.StatusEmprestimo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByStatus(StatusEmprestimo status);

    List<Emprestimo> findByUsuarioId(Long usuarioId);

    List<Emprestimo> findByLivroId(Long livroId);

    boolean existsByUsuarioAndLivroAndStatus(Usuario usuario, Livro livro, StatusEmprestimo statusEmprestimo);

    boolean existsByLivroAndStatus(Livro livro, StatusEmprestimo statusEmprestimo);

    boolean existsByUsuarioAndStatus(Usuario usuario, StatusEmprestimo statusEmprestimo);

    @Query("""
        SELECT new br.com.library_management_api.dto.response.LivroMaisEmprestadoResponse(
            e.livro.titulo,
            e.livro.autor,
            COUNT(e)
        )
        FROM Emprestimo e
        GROUP BY e.livro.id, e.livro.titulo, e.livro.autor
        ORDER BY COUNT(e) DESC
    """)
    List<LivroMaisEmprestadoResponse> listarLivrosMaisEmprestados(Pageable pageable);

    @Query("""
        SELECT new br.com.library_management_api.dto.response.EmprestimoAtivoResponse(
            e.livro.titulo,
            e.usuario.nome,
            e.dataEmprestimo,
            e.dataPrevistaDevolucao,
            e.status
        )
        FROM Emprestimo e
        WHERE e.status = br.com.library_management_api.enums.StatusEmprestimo.ATIVO
        ORDER BY e.dataPrevistaDevolucao ASC
    """)
    List<EmprestimoAtivoResponse> listarEmprestimosAtivos();
}
