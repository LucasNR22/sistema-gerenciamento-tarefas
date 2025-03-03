package com.desafio.tarefa.repository;

import com.desafio.tarefa.model.StatusTarefa;
import com.desafio.tarefa.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByUsuarioId(Long usuarioId);

    List<Tarefa> findByStatus(StatusTarefa status);

    List<Tarefa> findByUsuarioIdAndStatus(Long usuarioId, StatusTarefa status);

    long countByUsuarioId(Long usuarioId);
}