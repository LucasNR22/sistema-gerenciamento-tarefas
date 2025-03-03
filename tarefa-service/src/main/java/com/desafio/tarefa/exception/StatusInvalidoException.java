package com.desafio.tarefa.exception;

public class StatusInvalidoException extends RuntimeException {
    public StatusInvalidoException(String mensagem) {
        super(mensagem);
    }
}