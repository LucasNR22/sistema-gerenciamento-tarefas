package com.desafio.tarefa.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioClientTest {

    @Mock
    private UsuarioClient usuarioClient;

    @Test
    void deveVerificarExistenciaDoUsuario() {

        Long usuarioId = 1L;
        when(usuarioClient.usuarioExiste(usuarioId)).thenReturn(true);

        boolean resultado = usuarioClient.usuarioExiste(usuarioId);

        assertTrue(resultado);
        verify(usuarioClient, times(1)).usuarioExiste(usuarioId);
    }
}