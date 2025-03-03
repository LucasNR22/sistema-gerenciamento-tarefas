package com.desafio.tarefa.service;

import com.desafio.tarefa.client.UsuarioClient;
import com.desafio.tarefa.dto.TarefaDTO;
import com.desafio.tarefa.exception.StatusInvalidoException;
import com.desafio.tarefa.exception.TarefaNaoEncontradaException;
import com.desafio.tarefa.exception.UsuarioNaoExisteException;
import com.desafio.tarefa.model.StatusTarefa;
import com.desafio.tarefa.model.Tarefa;
import com.desafio.tarefa.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioClient usuarioClient;

    @Autowired
    public TarefaService(TarefaRepository tarefaRepository, UsuarioClient usuarioClient) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioClient = usuarioClient;
    }

    private TarefaDTO converterParaDTO(Tarefa tarefa) {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(tarefa.getId());
        dto.setTitulo(tarefa.getTitulo());
        dto.setDescricao(tarefa.getDescricao());
        dto.setStatus(tarefa.getStatus());
        dto.setDataCriacao(tarefa.getDataCriacao());
        dto.setDataLimite(tarefa.getDataLimite());
        dto.setUsuarioId(tarefa.getUsuarioId());
        return dto;
    }

    private Tarefa converterParaEntidade(TarefaDTO dto) {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(dto.getId());
        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setStatus(dto.getStatus());
        tarefa.setDataLimite(dto.getDataLimite());
        tarefa.setUsuarioId(dto.getUsuarioId());
        // A data de criação é definida automaticamente no prePersist
        return tarefa;
    }

    public List<TarefaDTO> listarTodas() {
        return tarefaRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public TarefaDTO buscarPorId(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada com o ID: " + id));
        return converterParaDTO(tarefa);
    }

    public List<TarefaDTO> filtrarPorUsuario(Long usuarioId) {
        verificarUsuarioExiste(usuarioId);
        return tarefaRepository.findByUsuarioId(usuarioId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<TarefaDTO> filtrarPorStatus(StatusTarefa status) {
        return tarefaRepository.findByStatus(status).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<TarefaDTO> filtrarPorUsuarioEStatus(Long usuarioId, StatusTarefa status) {
        verificarUsuarioExiste(usuarioId);
        return tarefaRepository.findByUsuarioIdAndStatus(usuarioId, status).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TarefaDTO criar(TarefaDTO tarefaDTO) {
        verificarUsuarioExiste(tarefaDTO.getUsuarioId());
        
        Tarefa tarefa = converterParaEntidade(tarefaDTO);
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        return converterParaDTO(tarefaSalva);
    }

    @Transactional
    public TarefaDTO atualizar(Long id, TarefaDTO tarefaDTO) {
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada com o ID: " + id));

        // Verificar se tarefa pode ser editada com base no status
        if (tarefaExistente.getStatus() == StatusTarefa.CONCLUIDO) {
            throw new StatusInvalidoException("Tarefas com status 'CONCLUIDO' não podem ser editadas");
        }

        // Verificar se o usuário existe
        verificarUsuarioExiste(tarefaDTO.getUsuarioId());

        // Atualizar os campos da tarefa
        tarefaExistente.setTitulo(tarefaDTO.getTitulo());
        tarefaExistente.setDescricao(tarefaDTO.getDescricao());
        tarefaExistente.setStatus(tarefaDTO.getStatus());
        tarefaExistente.setDataLimite(tarefaDTO.getDataLimite());
        tarefaExistente.setUsuarioId(tarefaDTO.getUsuarioId());

        Tarefa tarefaAtualizada = tarefaRepository.save(tarefaExistente);
        return converterParaDTO(tarefaAtualizada);
    }

    @Transactional
    public void excluir(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new TarefaNaoEncontradaException("Tarefa não encontrada com o ID: " + id);
        }
        
        tarefaRepository.deleteById(id);
    }

    // Verifica se há tarefas associadas a um usuário
    public boolean temTarefasDoUsuario(Long usuarioId) {
        return tarefaRepository.countByUsuarioId(usuarioId) > 0;
    }

    // Métodos auxiliares
    private void verificarUsuarioExiste(Long usuarioId) {
        try {
            boolean existe = usuarioClient.usuarioExiste(usuarioId);
            if (!existe) {
                throw new UsuarioNaoExisteException("Usuário não encontrado com o ID: " + usuarioId);
            }
        } catch (Exception e) {
            throw new UsuarioNaoExisteException("Erro ao verificar existência do usuário: " + e.getMessage());
        }
    }

}