package com.desafio.usuario.controller;

import com.desafio.usuario.dto.UsuarioDTO;
import com.desafio.usuario.exception.EmailDuplicadoException;
import com.desafio.usuario.exception.UsuarioNaoEncontradoException;
import com.desafio.usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        // Dados de teste
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNome("Teste Usuario");
        usuarioDTO.setEmail("teste@email.com");
        usuarioDTO.setDataCriacao(LocalDateTime.now());
    }

    @Test
    void deveListarTodosUsuarios() throws Exception {
        
        List<UsuarioDTO> usuarios = Arrays.asList(usuarioDTO);
        when(usuarioService.listarTodos()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nome", is("Teste Usuario")))
                .andExpect(jsonPath("$[0].email", is("teste@email.com")));

        verify(usuarioService, times(1)).listarTodos();
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        
        when(usuarioService.buscarPeloId(anyLong())).thenReturn(usuarioDTO);

        mockMvc.perform(get("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Teste Usuario")))
                .andExpect(jsonPath("$.email", is("teste@email.com")));

        verify(usuarioService, times(1)).buscarPeloId(1L);
    }

    @Test
    void deveCriarUsuario() throws Exception {
        
        UsuarioDTO novoUsuario = new UsuarioDTO();
        novoUsuario.setNome("Novo Usuario");
        novoUsuario.setEmail("novo@email.com");

        when(usuarioService.criar(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Teste Usuario")))
                .andExpect(jsonPath("$.email", is("teste@email.com")));

        verify(usuarioService, times(1)).criar(any(UsuarioDTO.class));
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        
        UsuarioDTO usuarioAtualizado = new UsuarioDTO();
        usuarioAtualizado.setNome("Usuario Atualizado");
        usuarioAtualizado.setEmail("atualizado@email.com");

        when(usuarioService.atualizar(anyLong(), any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Teste Usuario")))
                .andExpect(jsonPath("$.email", is("teste@email.com")));

        verify(usuarioService, times(1)).atualizar(eq(1L), any(UsuarioDTO.class));
    }

    @Test
    void deveExcluirUsuario() throws Exception {
        
        doNothing().when(usuarioService).excluir(anyLong());

        mockMvc.perform(delete("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).excluir(1L);
    }


    @Test
    void deveVerificarSeUsuarioExiste() throws Exception {
        
        when(usuarioService.usuarioExiste(anyLong())).thenReturn(true);

        mockMvc.perform(get("/api/usuarios/existe/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(usuarioService, times(1)).usuarioExiste(1L);
    }

    @Test
    void deveVerificarSeUsuarioNaoExiste() throws Exception {
        
        when(usuarioService.usuarioExiste(anyLong())).thenReturn(false);

        mockMvc.perform(get("/api/usuarios/existe/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(usuarioService, times(1)).usuarioExiste(999L);
    }
}