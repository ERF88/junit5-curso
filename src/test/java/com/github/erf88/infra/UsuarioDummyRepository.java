package com.github.erf88.infra;

import com.github.erf88.domain.Usuario;
import com.github.erf88.repository.UsuarioRepository;

import java.util.Optional;

import static com.github.erf88.domain.builders.UsuarioBuilder.umUsuario;

public class UsuarioDummyRepository implements UsuarioRepository {
    @Override
    public Usuario salvar(Usuario usuario) {
        return umUsuario()
                .comNome(usuario.getNome())
                .comEmail(usuario.getEmail())
                .comSenha(usuario.getSenha())
                .agora();
    }

    @Override
    public Optional<Usuario> getUserByEmail(String email) {
        if("usuario@mail.com".equals(email)) {
            return Optional.of(umUsuario().comEmail(email).agora());
        }
        return Optional.empty();
    }
}
