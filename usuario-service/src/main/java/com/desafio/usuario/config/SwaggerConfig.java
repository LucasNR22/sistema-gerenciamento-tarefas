package com.desafio.usuario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                    .info(new Info()
                        .title("API de Gerenciamento de Usuários")
                        .version("1.0")
                        .description("API REST para o microserviço de gerenciamento de usuários"));
    }
}