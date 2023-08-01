package com.github.erf88.infra;

import com.github.erf88.domain.Usuario;
import com.github.erf88.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioMemoryRepository implements UsuarioRepository {

    private List<Usuario> usuarios;
    private Long idAtual;

    public UsuarioMemoryRepository() {
        idAtual = 0L;
        usuarios = new ArrayList<>();
        salvar(new Usuario(null, "User #1", "user@mail.com", "123456"));
    }
    @Override
    public Usuario salvar(Usuario usuario) {
        Usuario novoUsuario = new Usuario(proximoId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha());
        usuarios.add(usuario);
        return novoUsuario;
    }

    @Override
    public Optional<Usuario> getUserByEmail(String email) {
        return usuarios.stream()
                .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    private Long proximoId() {
        return idAtual++;
    }
}
