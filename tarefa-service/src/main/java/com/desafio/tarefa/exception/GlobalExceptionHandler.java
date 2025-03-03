package com.desafio.tarefa.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TarefaNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> handleTarefaNaoEncontradaException(TarefaNaoEncontradaException ex) {
        ErroResponse erro = new ErroResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioNaoExisteException.class)
    public ResponseEntity<ErroResponse> handleUsuarioNaoExisteException(UsuarioNaoExisteException ex) {
        ErroResponse erro = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StatusInvalidoException.class)
    public ResponseEntity<ErroResponse> handleStatusInvalidoException(StatusInvalidoException ex) {
        ErroResponse erro = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErroResponse> handleFeignException(FeignException ex) {
        ErroResponse erro = new ErroResponse(
                ex.status(),
                "Erro ao comunicar com o serviço de usuários: " + ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(erro, HttpStatus.valueOf(ex.status()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Classe interna para representar a resposta de erro
    public static class ErroResponse {
        private int status;
        private String mensagem;
        private LocalDateTime timestamp;

        public ErroResponse(int status, String mensagem, LocalDateTime timestamp) {
            this.status = status;
            this.mensagem = mensagem;
            this.timestamp = timestamp;
        }

        // Getters
        public int getStatus() {
            return status;
        }

        public String getMensagem() {
            return mensagem;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}