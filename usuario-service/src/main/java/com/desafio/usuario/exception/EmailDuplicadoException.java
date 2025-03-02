package com.desafio.usuario.exception;

public class EmailDuplicadoException extends RuntimeException{
    public EmailDuplicadoException(String mensagem){
        super(mensagem);
    }
}
