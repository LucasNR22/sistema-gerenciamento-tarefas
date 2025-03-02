package com.desafio.usuario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "cd_usuario", nullable = false, unique = true)
    private Long id;

    @NotBlank(message = "Nome obrigatório!")
    @Column(name = "nm_usuario",nullable = false)
    private String nome;

    @NotBlank(message = "Email obrigatório!")
    @Email(message = "Email deve ser válido!")
    @Column(name = "nm_email", nullable = false, unique = true)
    private String email;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    public void prePersist() {
        dataCriacao = LocalDateTime.now();
    }
}