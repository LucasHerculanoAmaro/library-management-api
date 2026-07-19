package br.com.library_management_api.entity;

import br.com.library_management_api.entity.Livro;
import br.com.library_management_api.entity.Usuario;
import br.com.library_management_api.enums.StatusEmprestimo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "emprestimos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Column(nullable = false)
    private LocalDate dataEmprestimo;

    @Column(nullable = false)
    private LocalDate dataPrevistaDevolucao;

    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEmprestimo status;

    @PrePersist
    public void prePersist() {
        this.dataEmprestimo = LocalDate.now();

        if (this.dataPrevistaDevolucao == null) {
            this.dataPrevistaDevolucao = dataEmprestimo.plusDays(7);
        }

        this.status = StatusEmprestimo.ATIVO;
    }
}