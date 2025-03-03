package com.desafio.usuario.service;

import com.desafio.usuario.dto.UsuarioDTO;
import com.desafio.usuario.exception.EmailDuplicadoException;
import com.desafio.usuario.exception.UsuarioNaoEncontradoException;
import com.desafio.usuario.model.Usuario;
import com.desafio.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        // Configurando dados de teste
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste Usuario");
        usuario.setEmail("teste@email.com");
        usuario.setDataCriacao(LocalDateTime.now());

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNome("Teste Usuario");
        usuarioDTO.setEmail("teste@email.com");
        usuarioDTO.setDataCriacao(usuario.getDataCriacao());
    }

    @Test
    void deveListarTodosUsuarios() {
        // Given
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // When
        List<UsuarioDTO> resultado = usuarioService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(usuario.getId(), resultado.get(0).getId());
        assertEquals(usuario.getNome(), resultado.get(0).getNome());
        assertEquals(usuario.getEmail(), resultado.get(0).getEmail());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarUsuarioPeloId() {
        // Given
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        // When
        UsuarioDTO resultado = usuarioService.buscarPeloId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Given
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When/Then
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            usuarioService.buscarPeloId(1L);
        });
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void deveCriarUsuario() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        UsuarioDTO resultado = usuarioService.criar(usuarioDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioDTO.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // When/Then
        assertThrows(EmailDuplicadoException.class, () -> {
            usuarioService.criar(usuarioDTO);
        });
        verify(usuarioRepository, times(1)).existsByEmail(usuarioDTO.getEmail());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveAtualizarUsuario() {
        // Given
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNome("Nome Antigo");
        usuarioExistente.setEmail("antigo@email.com");
        usuarioExistente.setDataCriacao(LocalDateTime.now().minusDays(1));

        UsuarioDTO usuarioAtualizado = new UsuarioDTO();
        usuarioAtualizado.setNome("Nome Novo");
        usuarioAtualizado.setEmail("novo@email.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.existsByEmail(usuarioAtualizado.getEmail())).thenReturn(false);
        
        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome(usuarioAtualizado.getNome());
        usuarioSalvo.setEmail(usuarioAtualizado.getEmail());
        usuarioSalvo.setDataCriacao(usuarioExistente.getDataCriacao());
        
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // When
        UsuarioDTO resultado = usuarioService.atualizar(1L, usuarioAtualizado);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(usuarioAtualizado.getNome(), resultado.getNome());
        assertEquals(usuarioAtualizado.getEmail(), resultado.getEmail());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).existsByEmail(usuarioAtualizado.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarEmailDuplicado() {
        // Given
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNome("Nome Antigo");
        usuarioExistente.setEmail("antigo@email.com");

        UsuarioDTO usuarioAtualizado = new UsuarioDTO();
        usuarioAtualizado.setNome("Nome Novo");
        usuarioAtualizado.setEmail("duplicado@email.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.existsByEmail(usuarioAtualizado.getEmail())).thenReturn(true);

        // When/Then
        assertThrows(EmailDuplicadoException.class, () -> {
            usuarioService.atualizar(1L, usuarioAtualizado);
        });
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).existsByEmail(usuarioAtualizado.getEmail());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveExcluirUsuario() {
        // Given
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        // When
        usuarioService.excluir(1L);

        // Then
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoExcluirUsuarioInexistente() {
        // Given
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        // When/Then
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            usuarioService.excluir(1L);
        });
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test
    void deveVerificarSeUsuarioExiste() {
        // Given
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        // When
        boolean resultado = usuarioService.usuarioExiste(1L);

        // Then
        assertTrue(resultado);
        verify(usuarioRepository, times(1)).existsById(1L);
    }

    @Test
    void deveVerificarSeUsuarioNaoExiste() {
        // Given
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        // When
        boolean resultado = usuarioService.usuarioExiste(1L);

        // Then
        assertFalse(resultado);
        verify(usuarioRepository, times(1)).existsById(1L);
    }
}