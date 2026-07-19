package br.com.library_management_api.entity;

import br.com.library_management_api.enums.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    // Identificador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campos de negócio
    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String cpf;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    @Column(nullable = false)
    private Boolean ativo = true;

    // Relacionamento
    @OneToMany(mappedBy = "usuario")
    private List<Emprestimo> emprestimos;

    // Campo de auditoria
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    // Método de ciclo de vida
    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
    }
}
