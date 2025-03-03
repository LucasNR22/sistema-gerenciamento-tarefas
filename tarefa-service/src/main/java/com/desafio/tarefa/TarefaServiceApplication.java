package com.desafio.tarefa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TarefaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TarefaServiceApplication.class, args);
	}
}