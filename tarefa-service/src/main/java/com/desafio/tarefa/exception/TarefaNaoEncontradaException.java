package com.desafio.tarefa.exception;

public class TarefaNaoEncontradaException extends RuntimeException {
    public TarefaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}