package br.com.library_management_api.repository;

import br.com.library_management_api.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    long findById(long id);
    boolean existsByIsbn(String isbn);
}
