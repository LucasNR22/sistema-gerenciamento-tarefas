package com.desafio.tarefa.service;

import com.desafio.tarefa.client.UsuarioClient;
import com.desafio.tarefa.dto.TarefaDTO;
import com.desafio.tarefa.exception.StatusInvalidoException;
import com.desafio.tarefa.exception.TarefaNaoEncontradaException;
import com.desafio.tarefa.exception.UsuarioNaoExisteException;
import com.desafio.tarefa.model.StatusTarefa;
import com.desafio.tarefa.model.Tarefa;
import com.desafio.tarefa.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private TarefaService tarefaService;

    private Tarefa tarefa;
    private TarefaDTO tarefaDTO;
    private final Long TAREFA_ID = 1L;
    private final Long USUARIO_ID = 10L;

    @BeforeEach
    void setUp() {
        // Configurar objetos de teste
        tarefa = new Tarefa();
        tarefa.setId(TAREFA_ID);
        tarefa.setTitulo("Tarefa de Teste");
        tarefa.setDescricao("Descrição da tarefa de teste");
        tarefa.setStatus(StatusTarefa.PENDENTE);
        tarefa.setDataCriacao(LocalDateTime.now());
        tarefa.setDataLimite(LocalDate.now().plusDays(7));
        tarefa.setUsuarioId(USUARIO_ID);

        tarefaDTO = new TarefaDTO();
        tarefaDTO.setId(TAREFA_ID);
        tarefaDTO.setTitulo("Tarefa de Teste");
        tarefaDTO.setDescricao("Descrição da tarefa de teste");
        tarefaDTO.setStatus(StatusTarefa.PENDENTE);
        tarefaDTO.setDataCriacao(LocalDateTime.now());
        tarefaDTO.setDataLimite(LocalDate.now().plusDays(7));
        tarefaDTO.setUsuarioId(USUARIO_ID);
    }

    @Test
    void deveListarTodasAsTarefas() {
        
        List<Tarefa> tarefas = Arrays.asList(tarefa);
        when(tarefaRepository.findAll()).thenReturn(tarefas);

        List<TarefaDTO> resultado = tarefaService.listarTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(TAREFA_ID, resultado.get(0).getId());
        verify(tarefaRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarTarefaPorId() {
        
        when(tarefaRepository.findById(TAREFA_ID)).thenReturn(Optional.of(tarefa));

        TarefaDTO resultado = tarefaService.buscarPorId(TAREFA_ID);

        assertNotNull(resultado);
        assertEquals(TAREFA_ID, resultado.getId());
        assertEquals("Tarefa de Teste", resultado.getTitulo());
        verify(tarefaRepository, times(1)).findById(TAREFA_ID);
    }

    @Test
    void deveFiltrarTarefasPorUsuario() {
        
        List<Tarefa> tarefas = Arrays.asList(tarefa);
        when(usuarioClient.usuarioExiste(USUARIO_ID)).thenReturn(true);
        when(tarefaRepository.findByUsuarioId(USUARIO_ID)).thenReturn(tarefas);

        List<TarefaDTO> resultado = tarefaService.filtrarPorUsuario(USUARIO_ID);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(USUARIO_ID, resultado.get(0).getUsuarioId());
        verify(usuarioClient, times(1)).usuarioExiste(USUARIO_ID);
        verify(tarefaRepository, times(1)).findByUsuarioId(USUARIO_ID);
    }

    @Test
    void deveFiltrarTarefasPorStatus() {
        
        List<Tarefa> tarefas = Arrays.asList(tarefa);
        when(tarefaRepository.findByStatus(StatusTarefa.PENDENTE)).thenReturn(tarefas);

        List<TarefaDTO> resultado = tarefaService.filtrarPorStatus(StatusTarefa.PENDENTE);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(StatusTarefa.PENDENTE, resultado.get(0).getStatus());
        verify(tarefaRepository, times(1)).findByStatus(StatusTarefa.PENDENTE);
    }

    @Test
    void deveFiltrarTarefasPorUsuarioEStatus() {
        
        List<Tarefa> tarefas = Arrays.asList(tarefa);
        when(usuarioClient.usuarioExiste(USUARIO_ID)).thenReturn(true);
        when(tarefaRepository.findByUsuarioIdAndStatus(USUARIO_ID, StatusTarefa.PENDENTE)).thenReturn(tarefas);

        List<TarefaDTO> resultado = tarefaService.filtrarPorUsuarioEStatus(USUARIO_ID, StatusTarefa.PENDENTE);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(USUARIO_ID, resultado.get(0).getUsuarioId());
        assertEquals(StatusTarefa.PENDENTE, resultado.get(0).getStatus());
        verify(usuarioClient, times(1)).usuarioExiste(USUARIO_ID);
        verify(tarefaRepository, times(1)).findByUsuarioIdAndStatus(USUARIO_ID, StatusTarefa.PENDENTE);
    }

    @Test
    void deveCriarTarefa() {
        
        when(usuarioClient.usuarioExiste(USUARIO_ID)).thenReturn(true);
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        TarefaDTO resultado = tarefaService.criar(tarefaDTO);

        assertNotNull(resultado);
        assertEquals(TAREFA_ID, resultado.getId());
        assertEquals("Tarefa de Teste", resultado.getTitulo());
        verify(usuarioClient, times(1)).usuarioExiste(USUARIO_ID);
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void deveAtualizarTarefa() {
        
        when(tarefaRepository.findById(TAREFA_ID)).thenReturn(Optional.of(tarefa));
        when(usuarioClient.usuarioExiste(USUARIO_ID)).thenReturn(true);
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        // Modificar DTO para atualização
        tarefaDTO.setTitulo("Tarefa Atualizada");
        tarefaDTO.setStatus(StatusTarefa.EM_ANDAMENTO);

        TarefaDTO resultado = tarefaService.atualizar(TAREFA_ID, tarefaDTO);

        assertNotNull(resultado);
        assertEquals(TAREFA_ID, resultado.getId());
        assertEquals("Tarefa Atualizada", resultado.getTitulo());
        assertEquals(StatusTarefa.EM_ANDAMENTO, resultado.getStatus());
        verify(tarefaRepository, times(1)).findById(TAREFA_ID);
        verify(usuarioClient, times(1)).usuarioExiste(USUARIO_ID);
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void deveExcluirTarefa() {
        
        when(tarefaRepository.existsById(TAREFA_ID)).thenReturn(true);
        doNothing().when(tarefaRepository).deleteById(TAREFA_ID);

        tarefaService.excluir(TAREFA_ID);

        verify(tarefaRepository, times(1)).existsById(TAREFA_ID);
        verify(tarefaRepository, times(1)).deleteById(TAREFA_ID);
    }

    @Test
    void deveVerificarSeTarefaExisteParaUsuario() {
        
        when(tarefaRepository.countByUsuarioId(USUARIO_ID)).thenReturn(1L);

        boolean resultado = tarefaService.temTarefasDoUsuario(USUARIO_ID);

        assertTrue(resultado);
        verify(tarefaRepository, times(1)).countByUsuarioId(USUARIO_ID);
    }

    @Test
    void deveRetornarFalseQuandoUsuarioNaoTemTarefas() {
        
        when(tarefaRepository.countByUsuarioId(USUARIO_ID)).thenReturn(0L);

        boolean resultado = tarefaService.temTarefasDoUsuario(USUARIO_ID);

        assertFalse(resultado);
        verify(tarefaRepository, times(1)).countByUsuarioId(USUARIO_ID);
    }
}