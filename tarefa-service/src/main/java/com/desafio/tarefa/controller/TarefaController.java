package com.desafio.tarefa.controller;

import com.desafio.tarefa.dto.TarefaDTO;
import com.desafio.tarefa.model.StatusTarefa;
import com.desafio.tarefa.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
@Tag(name = "Tarefas", description = "API para gerenciamento de tarefas")
@CrossOrigin(origins = "*")
public class TarefaController {

    private final TarefaService tarefaService;

    @Autowired
    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as tarefas")
    public ResponseEntity<List<TarefaDTO>> listarTodas() {
        List<TarefaDTO> tarefas = tarefaService.listarTodas();
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por ID")
    public ResponseEntity<TarefaDTO> buscarPorId(@PathVariable Long id) {
        TarefaDTO tarefa = tarefaService.buscarPorId(id);
        return ResponseEntity.ok(tarefa);
    }

    @GetMapping("/filtrar")
    @Operation(summary = "Filtrar tarefas por usuário e/ou status")
    public ResponseEntity<List<TarefaDTO>> filtrarTarefas(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) StatusTarefa status) {
        
        List<TarefaDTO> tarefas;
        
        if (usuarioId != null && status != null) {
            tarefas = tarefaService.filtrarPorUsuarioEStatus(usuarioId, status);
        } else if (usuarioId != null) {
            tarefas = tarefaService.filtrarPorUsuario(usuarioId);
        } else if (status != null) {
            tarefas = tarefaService.filtrarPorStatus(status);
        } else {
            tarefas = tarefaService.listarTodas();
        }
        
        return ResponseEntity.ok(tarefas);
    }

    @PostMapping
    @Operation(summary = "Criar nova tarefa")
    public ResponseEntity<TarefaDTO> criar(@Valid @RequestBody TarefaDTO tarefaDTO) {
        TarefaDTO novaTarefa = tarefaService.criar(tarefaDTO);
        return new ResponseEntity<>(novaTarefa, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa existente")
    public ResponseEntity<TarefaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TarefaDTO tarefaDTO) {
        TarefaDTO tarefaAtualizada = tarefaService.atualizar(id, tarefaDTO);
        return ResponseEntity.ok(tarefaAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tarefa")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        tarefaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verificar-usuario/{usuarioId}")
    @Operation(summary = "Verificar se usuário possui tarefas", description = "Endpoint utilizado internamente pelo serviço de usuários")
    public ResponseEntity<Boolean> verificarTarefasDoUsuario(@PathVariable Long usuarioId) {
        boolean temTarefas = tarefaService.temTarefasDoUsuario(usuarioId);
        return ResponseEntity.ok(temTarefas);
    }
}