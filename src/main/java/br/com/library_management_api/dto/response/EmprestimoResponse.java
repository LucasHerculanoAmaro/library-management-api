package br.com.library_management_api.dto.response;

import br.com.library_management_api.enums.StatusEmprestimo;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmprestimoResponse {

    private Long id;

    private String usuario;

    private String livro;

    private LocalDate dataEmprestimo;

    private LocalDate dataPrevistaDevolucao;

    private LocalDate dataDevolucao;

    private StatusEmprestimo status;

    //private Boolean devolvido;

}