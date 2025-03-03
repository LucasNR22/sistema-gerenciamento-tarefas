package com.desafio.tarefa.exception;

public class UsuarioNaoExisteException extends RuntimeException {
    public UsuarioNaoExisteException(String mensagem) {
        super(mensagem);
    }
}