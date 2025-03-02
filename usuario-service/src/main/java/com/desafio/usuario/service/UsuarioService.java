package com.desafio.usuario.service;

import com.desafio.usuario.dto.UsuarioDTO;
import com.desafio.usuario.exception.EmailDuplicadoException;
import com.desafio.usuario.exception.UsuarioNaoEncontradoException;
import com.desafio.usuario.model.Usuario;
import com.desafio.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private UsuarioDTO converterParaDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setDataCriacao(usuario.getDataCriacao());
        return dto;
    }

    private Usuario converterParaEntidade(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        // A data de criacao eh definida automaticamente no prePersist
        return usuario;
    }

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO buscarPeloId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com o ID: " + id));
        return converterParaDTO(usuario);
    }

    @Transactional
    public UsuarioDTO criar(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new EmailDuplicadoException("Email já cadastrado: " + usuarioDTO.getEmail());
        }

        Usuario usuario = converterParaEntidade(usuarioDTO);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return converterParaDTO(usuarioSalvo);
    }

    @Transactional
    public UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com o ID: " + id));

        // Verifica se email novo ja existe para outro usuário
        if (!usuarioExistente.getEmail().equals(usuarioDTO.getEmail()) &&
                usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new EmailDuplicadoException("Email já cadastrado: " + usuarioDTO.getEmail());
        }

        usuarioExistente.setNome(usuarioDTO.getNome());
        usuarioExistente.setEmail(usuarioDTO.getEmail());

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return converterParaDTO(usuarioAtualizado);
    }

    @Transactional
    public void excluir(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado com o ID: " + id);
        }

        usuarioRepository.deleteById(id);
    }

    // Usado pelo TarefaService
    public boolean usuarioExiste(Long id) {
        return usuarioRepository.existsById(id);
    }

    /*
     * Criar UsuarioRepository
     * Criar a injhecao de dependencia pro UsuarioRepository
     * Criar conversores de transação
     * Criar os CRUDs pra usuario
     * */

}