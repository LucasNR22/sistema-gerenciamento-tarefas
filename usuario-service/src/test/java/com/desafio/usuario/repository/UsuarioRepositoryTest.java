package com.desafio.usuario.repository;

import com.desafio.usuario.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void deveEncontrarUsuarioPorEmail() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@exemplo.com");
        usuario.setDataCriacao(LocalDateTime.now());
        usuarioRepository.save(usuario);

        // Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("teste@exemplo.com");

        // Assert
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("Teste", usuarioEncontrado.get().getNome());
    }

    @Test
    public void naoDeveEncontrarUsuarioPorEmailInexistente() {
        // Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("naoexiste@exemplo.com");

        // Assert
        assertFalse(usuarioEncontrado.isPresent());
    }

    @Test
    public void deveVerificarExistenciaPorEmail() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste2@exemplo.com");
        usuario.setDataCriacao(LocalDateTime.now());
        usuarioRepository.save(usuario);

        // Act & Assert
        assertTrue(usuarioRepository.existsByEmail("teste2@exemplo.com"));
        assertFalse(usuarioRepository.existsByEmail("naoexiste@exemplo.com"));
    }
}