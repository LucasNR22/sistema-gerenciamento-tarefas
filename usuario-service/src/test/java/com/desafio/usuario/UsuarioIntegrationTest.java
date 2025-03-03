package com.desafio.usuario;

import com.desafio.usuario.model.Usuario;
import com.desafio.usuario.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @AfterEach
    public void cleanup() {
        usuarioRepository.deleteAll();
    }

    @Test
    public void deveRealizarCicloCompletoUsuario() throws Exception {
        // 1. Criar usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Teste Integração");
        novoUsuario.setEmail("teste.integracao@exemplo.com");

        String responseString = mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Teste Integração"))
                .andExpect(jsonPath("$.email").value("teste.integracao@exemplo.com"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        Usuario usuarioCriado = objectMapper.readValue(responseString, Usuario.class);
        Long userId = usuarioCriado.getId();

        // 2. Buscar usuário por ID
        mockMvc.perform(get("/api/usuarios/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nome").value("Teste Integração"));

        // 3. Listar todos os usuários
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(userId));

        // 4. Atualizar usuário
        usuarioCriado.setNome("Nome Atualizado");
        mockMvc.perform(put("/api/usuarios/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioCriado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.email").value("teste.integracao@exemplo.com"));

        // 5. Verificar atualização
        mockMvc.perform(get("/api/usuarios/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"));

        // 6. Deletar usuário
        mockMvc.perform(delete("/api/usuarios/" + userId))
                .andExpect(status().isNoContent());

        // 7. Verificar que usuário foi deletado
        mockMvc.perform(get("/api/usuarios/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void naoDeveCriarUsuarioComEmailDuplicado() throws Exception {
        // 1. Criar primeiro usuário
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Primeiro Usuário");
        usuario1.setEmail("duplicado@exemplo.com");

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario1)))
                .andExpect(status().isCreated());

        // 2. Tentar criar segundo usuário com mesmo email
        Usuario usuario2 = new Usuario();
        usuario2.setNome("Segundo Usuário");
        usuario2.setEmail("duplicado@exemplo.com");

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").exists());
    }
}