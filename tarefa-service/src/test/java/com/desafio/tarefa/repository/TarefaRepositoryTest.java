package com.desafio.tarefa.repository;

import com.desafio.tarefa.model.StatusTarefa;
import com.desafio.tarefa.model.Tarefa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TarefaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Test
    public void testFindByUsuarioId() {
        // Dados de teste
        Tarefa tarefa1 = new Tarefa();
        tarefa1.setTitulo("Tarefa 1");
        tarefa1.setDescricao("Descrição da tarefa 1");
        tarefa1.setStatus(StatusTarefa.PENDENTE);
        tarefa1.setDataCriacao(LocalDateTime.now());
        tarefa1.setUsuarioId(1L);

        Tarefa tarefa2 = new Tarefa();
        tarefa2.setTitulo("Tarefa 2");
        tarefa2.setDescricao("Descrição da tarefa 2");
        tarefa2.setStatus(StatusTarefa.EM_ANDAMENTO);
        tarefa2.setDataCriacao(LocalDateTime.now());
        tarefa2.setUsuarioId(1L);

        Tarefa tarefa3 = new Tarefa();
        tarefa3.setTitulo("Tarefa 3");
        tarefa3.setDescricao("Descrição da tarefa 3");
        tarefa3.setStatus(StatusTarefa.PENDENTE);
        tarefa3.setDataCriacao(LocalDateTime.now());
        tarefa3.setUsuarioId(2L);

        // Persistir as tarefas
        entityManager.persist(tarefa1);
        entityManager.persist(tarefa2);
        entityManager.persist(tarefa3);
        entityManager.flush();

        // Executar o método
        List<Tarefa> tarefasDoUsuario1 = tarefaRepository.findByUsuarioId(1L);

        // Verificar resultados
        assertThat(tarefasDoUsuario1).hasSize(2);
        assertThat(tarefasDoUsuario1).extracting(Tarefa::getTitulo).containsExactlyInAnyOrder("Tarefa 1", "Tarefa 2");
        assertThat(tarefasDoUsuario1).extracting(Tarefa::getUsuarioId).containsOnly(1L);
    }

    @Test
    public void testFindByStatus() {
        // Dados de teste
        Tarefa tarefa1 = new Tarefa();
        tarefa1.setTitulo("Tarefa 1");
        tarefa1.setDescricao("Descrição da tarefa 1");
        tarefa1.setStatus(StatusTarefa.PENDENTE);
        tarefa1.setDataCriacao(LocalDateTime.now());
        tarefa1.setUsuarioId(1L);

        Tarefa tarefa2 = new Tarefa();
        tarefa2.setTitulo("Tarefa 2");
        tarefa2.setDescricao("Descrição da tarefa 2");
        tarefa2.setStatus(StatusTarefa.EM_ANDAMENTO);
        tarefa2.setDataCriacao(LocalDateTime.now());
        tarefa2.setUsuarioId(1L);

        Tarefa tarefa3 = new Tarefa();
        tarefa3.setTitulo("Tarefa 3");
        tarefa3.setDescricao("Descrição da tarefa 3");
        tarefa3.setStatus(StatusTarefa.PENDENTE);
        tarefa3.setDataCriacao(LocalDateTime.now());
        tarefa3.setUsuarioId(2L);

        // Persistir as tarefas
        entityManager.persist(tarefa1);
        entityManager.persist(tarefa2);
        entityManager.persist(tarefa3);
        entityManager.flush();

        // Executar o método
        List<Tarefa> tarefasPendentes = tarefaRepository.findByStatus(StatusTarefa.PENDENTE);

        // Verificar resultados
        assertThat(tarefasPendentes).hasSize(2);
        assertThat(tarefasPendentes).extracting(Tarefa::getTitulo).containsExactlyInAnyOrder("Tarefa 1", "Tarefa 3");
        assertThat(tarefasPendentes).extracting(Tarefa::getStatus).containsOnly(StatusTarefa.PENDENTE);
    }

    @Test
    public void testFindByUsuarioIdAndStatus() {
        // Dados de teste
        Tarefa tarefa1 = new Tarefa();
        tarefa1.setTitulo("Tarefa 1");
        tarefa1.setDescricao("Descrição da tarefa 1");
        tarefa1.setStatus(StatusTarefa.PENDENTE);
        tarefa1.setDataCriacao(LocalDateTime.now());
        tarefa1.setUsuarioId(1L);

        Tarefa tarefa2 = new Tarefa();
        tarefa2.setTitulo("Tarefa 2");
        tarefa2.setDescricao("Descrição da tarefa 2");
        tarefa2.setStatus(StatusTarefa.EM_ANDAMENTO);
        tarefa2.setDataCriacao(LocalDateTime.now());
        tarefa2.setUsuarioId(1L);

        Tarefa tarefa3 = new Tarefa();
        tarefa3.setTitulo("Tarefa 3");
        tarefa3.setDescricao("Descrição da tarefa 3");
        tarefa3.setStatus(StatusTarefa.PENDENTE);
        tarefa3.setDataCriacao(LocalDateTime.now());
        tarefa3.setUsuarioId(2L);

        // Persistir as tarefas
        entityManager.persist(tarefa1);
        entityManager.persist(tarefa2);
        entityManager.persist(tarefa3);
        entityManager.flush();

        // Executar o método
        List<Tarefa> tarefasUsuario1Pendentes = tarefaRepository.findByUsuarioIdAndStatus(1L, StatusTarefa.PENDENTE);

        // Verificar resultados
        assertThat(tarefasUsuario1Pendentes).hasSize(1);
        assertThat(tarefasUsuario1Pendentes.get(0).getTitulo()).isEqualTo("Tarefa 1");
        assertThat(tarefasUsuario1Pendentes.get(0).getUsuarioId()).isEqualTo(1L);
        assertThat(tarefasUsuario1Pendentes.get(0).getStatus()).isEqualTo(StatusTarefa.PENDENTE);
    }

    @Test
    public void testCountByUsuarioId() {
        // Dados de teste
        Tarefa tarefa1 = new Tarefa();
        tarefa1.setTitulo("Tarefa 1");
        tarefa1.setDescricao("Descrição da tarefa 1");
        tarefa1.setStatus(StatusTarefa.PENDENTE);
        tarefa1.setDataCriacao(LocalDateTime.now());
        tarefa1.setUsuarioId(1L);

        Tarefa tarefa2 = new Tarefa();
        tarefa2.setTitulo("Tarefa 2");
        tarefa2.setDescricao("Descrição da tarefa 2");
        tarefa2.setStatus(StatusTarefa.EM_ANDAMENTO);
        tarefa2.setDataCriacao(LocalDateTime.now());
        tarefa2.setUsuarioId(1L);

        Tarefa tarefa3 = new Tarefa();
        tarefa3.setTitulo("Tarefa 3");
        tarefa3.setDescricao("Descrição da tarefa 3");
        tarefa3.setStatus(StatusTarefa.PENDENTE);
        tarefa3.setDataCriacao(LocalDateTime.now());
        tarefa3.setUsuarioId(2L);

        // Persistir as tarefas
        entityManager.persist(tarefa1);
        entityManager.persist(tarefa2);
        entityManager.persist(tarefa3);
        entityManager.flush();

        // Executar o método
        long contagem1 = tarefaRepository.countByUsuarioId(1L);
        long contagem2 = tarefaRepository.countByUsuarioId(2L);
        long contagem3 = tarefaRepository.countByUsuarioId(3L);

        // Verificar resultados
        assertThat(contagem1).isEqualTo(2);
        assertThat(contagem2).isEqualTo(1);
        assertThat(contagem3).isEqualTo(0);
    }

    @Test
    public void testFindByUsuarioIdWithDataLimite() {
        // Dados de teste
        Tarefa tarefa1 = new Tarefa();
        tarefa1.setTitulo("Tarefa 1");
        tarefa1.setDescricao("Descrição da tarefa 1");
        tarefa1.setStatus(StatusTarefa.PENDENTE);
        tarefa1.setDataCriacao(LocalDateTime.now());
        tarefa1.setDataLimite(LocalDate.now().plusDays(5));
        tarefa1.setUsuarioId(1L);

        Tarefa tarefa2 = new Tarefa();
        tarefa2.setTitulo("Tarefa 2");
        tarefa2.setDescricao("Descrição da tarefa 2");
        tarefa2.setStatus(StatusTarefa.EM_ANDAMENTO);
        tarefa2.setDataCriacao(LocalDateTime.now());
        tarefa2.setDataLimite(LocalDate.now().plusDays(3));
        tarefa2.setUsuarioId(1L);

        // Persistir as tarefas
        entityManager.persist(tarefa1);
        entityManager.persist(tarefa2);
        entityManager.flush();

        // Executar o método
        List<Tarefa> tarefasDoUsuario1 = tarefaRepository.findByUsuarioId(1L);

        // Verificar resultados
        assertThat(tarefasDoUsuario1).hasSize(2);
        assertThat(tarefasDoUsuario1).extracting(Tarefa::getDataLimite).isNotNull();
    }

    @Test
    public void testFindByUsuarioIdWhenNoTarefasExist() {
        // Não criar nenhuma tarefa para o usuário

        // Executar o método
        List<Tarefa> tarefasDoUsuario = tarefaRepository.findByUsuarioId(999L);

        // Verificar resultados
        assertThat(tarefasDoUsuario).isEmpty();
    }
}