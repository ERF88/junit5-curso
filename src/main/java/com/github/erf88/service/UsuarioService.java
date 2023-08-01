package com.github.erf88.service;

import com.github.erf88.domain.Usuario;
import com.github.erf88.domain.exception.ValidationException;
import com.github.erf88.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public Usuario salvar(Usuario usuario) {
        usuarioRepository.getUserByEmail(usuario.getEmail()).ifPresent(user -> {
            throw new ValidationException(String.format("Usuário %s já cadastrado!", usuario.getEmail()));
        });
        return usuarioRepository.salvar(usuario);
    }

    public Optional<Usuario> getUsuarioPorEmail(String email) {
        return usuarioRepository.getUserByEmail(email);
    }

}
