package com.desafio.tarefa.dto;

import com.desafio.tarefa.model.StatusTarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaDTO {

    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    private String descricao;

    @NotNull(message = "O status é obrigatório")
    private StatusTarefa status;

    private LocalDateTime dataCriacao;

    private LocalDate dataLimite;

    @NotNull(message = "O usuário responsável é obrigatório")
    private Long usuarioId;
}