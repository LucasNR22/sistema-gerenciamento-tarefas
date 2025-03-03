package com.desafio.tarefa.controller;

import com.desafio.tarefa.dto.TarefaDTO;
import com.desafio.tarefa.exception.StatusInvalidoException;
import com.desafio.tarefa.exception.TarefaNaoEncontradaException;
import com.desafio.tarefa.exception.UsuarioNaoExisteException;
import com.desafio.tarefa.model.StatusTarefa;
import com.desafio.tarefa.service.TarefaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TarefaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TarefaService tarefaService;

    @InjectMocks
    private TarefaController tarefaController;

    private ObjectMapper objectMapper;
    private TarefaDTO tarefaDTO;
    private final Long TAREFA_ID = 1L;
    private final Long USUARIO_ID = 10L;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc e o manipulador global de exceções
        mockMvc = MockMvcBuilders.standaloneSetup(tarefaController)
                .setControllerAdvice(new com.desafio.tarefa.exception.GlobalExceptionHandler())
                .build();

        // Configurar ObjectMapper para lidar com LocalDateTime
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Configurar objetos de teste
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
    void deveListarTodasTarefas() throws Exception {
        
        List<TarefaDTO> tarefas = Arrays.asList(tarefaDTO);
        when(tarefaService.listarTodas()).thenReturn(tarefas);

        mockMvc.perform(get("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(TAREFA_ID.intValue())))
                .andExpect(jsonPath("$[0].titulo", is("Tarefa de Teste")));

        verify(tarefaService, times(1)).listarTodas();
    }

    @Test
    void deveBuscarTarefaPorId() throws Exception {
        
        when(tarefaService.buscarPorId(TAREFA_ID)).thenReturn(tarefaDTO);

        mockMvc.perform(get("/api/tarefas/{id}", TAREFA_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TAREFA_ID.intValue())))
                .andExpect(jsonPath("$.titulo", is("Tarefa de Teste")));

        verify(tarefaService, times(1)).buscarPorId(TAREFA_ID);
    }

    @Test
    void deveFiltrarTarefasPorUsuario() throws Exception {
        
        List<TarefaDTO> tarefas = Arrays.asList(tarefaDTO);
        when(tarefaService.filtrarPorUsuario(USUARIO_ID)).thenReturn(tarefas);

        mockMvc.perform(get("/api/tarefas/filtrar")
                .param("usuarioId", USUARIO_ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].usuarioId", is(USUARIO_ID.intValue())));

        verify(tarefaService, times(1)).filtrarPorUsuario(USUARIO_ID);
    }

    @Test
    void deveFiltrarTarefasPorStatus() throws Exception {
        
        List<TarefaDTO> tarefas = Arrays.asList(tarefaDTO);
        when(tarefaService.filtrarPorStatus(StatusTarefa.PENDENTE)).thenReturn(tarefas);

        mockMvc.perform(get("/api/tarefas/filtrar")
                .param("status", "PENDENTE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("PENDENTE")));

        verify(tarefaService, times(1)).filtrarPorStatus(StatusTarefa.PENDENTE);
    }

    @Test
    void deveFiltrarTarefasPorUsuarioEStatus() throws Exception {
        
        List<TarefaDTO> tarefas = Arrays.asList(tarefaDTO);
        when(tarefaService.filtrarPorUsuarioEStatus(USUARIO_ID, StatusTarefa.PENDENTE)).thenReturn(tarefas);

        mockMvc.perform(get("/api/tarefas/filtrar")
                .param("usuarioId", USUARIO_ID.toString())
                .param("status", "PENDENTE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].usuarioId", is(USUARIO_ID.intValue())))
                .andExpect(jsonPath("$[0].status", is("PENDENTE")));

        verify(tarefaService, times(1)).filtrarPorUsuarioEStatus(USUARIO_ID, StatusTarefa.PENDENTE);
    }

    @Test
    void deveCriarTarefa() throws Exception {
        
        when(tarefaService.criar(any(TarefaDTO.class))).thenReturn(tarefaDTO);

        mockMvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(TAREFA_ID.intValue())))
                .andExpect(jsonPath("$.titulo", is("Tarefa de Teste")));

        verify(tarefaService, times(1)).criar(any(TarefaDTO.class));
    }

    @Test
    void deveTratarErroDeValidacaoAoCriarTarefa() throws Exception {
        // objeto sem título
        tarefaDTO.setTitulo(null);

        mockMvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.titulo").exists());

        verify(tarefaService, never()).criar(any(TarefaDTO.class));
    }

    @Test
    void deveAtualizarTarefa() throws Exception {
        
        tarefaDTO.setTitulo("Tarefa Atualizada");
        when(tarefaService.atualizar(eq(TAREFA_ID), any(TarefaDTO.class))).thenReturn(tarefaDTO);

        mockMvc.perform(put("/api/tarefas/{id}", TAREFA_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TAREFA_ID.intValue())))
                .andExpect(jsonPath("$.titulo", is("Tarefa Atualizada")));

        verify(tarefaService, times(1)).atualizar(eq(TAREFA_ID), any(TarefaDTO.class));
    }

    @Test
    void deveExcluirTarefa() throws Exception {
        
        doNothing().when(tarefaService).excluir(TAREFA_ID);

        mockMvc.perform(delete("/api/tarefas/{id}", TAREFA_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(tarefaService, times(1)).excluir(TAREFA_ID);
    }

    @Test
    void deveVerificarTarefasDoUsuario() throws Exception {
        
        when(tarefaService.temTarefasDoUsuario(USUARIO_ID)).thenReturn(true);

        mockMvc.perform(get("/api/tarefas/verificar-usuario/{usuarioId}", USUARIO_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(tarefaService, times(1)).temTarefasDoUsuario(USUARIO_ID);
    }
}