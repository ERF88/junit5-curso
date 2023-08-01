package com.github.erf88.service;

import com.github.erf88.domain.Usuario;
import com.github.erf88.domain.exception.ValidationException;
import com.github.erf88.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.github.erf88.domain.builders.UsuarioBuilder.umUsuario;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("service")
@Tag("usuario")
@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks private UsuarioService usuarioService;

    @Mock private UsuarioRepository repository;

    @Test
    void deveRetornarEmptyQuandoUsuarioInexistente() {
//        Mockito.when(repository.getUserByEmail("mail@mail.com")).thenReturn(Optional.empty());

        Optional<Usuario> usuario = usuarioService.getUsuarioPorEmail("mail@mail.com");
        assertTrue(usuario.isEmpty());
    }

    @Test
    void deveRetornarUsuarioPorEmail() {
        when(repository.getUserByEmail("mail@mail.com")).thenReturn(Optional.of(umUsuario().agora()));

        Optional<Usuario> usuario = usuarioService.getUsuarioPorEmail("mail@mail.com");
        assertTrue(usuario.isPresent());

        verify(repository, atLeastOnce()).getUserByEmail("mail@mail.com");
        verify(repository, never()).getUserByEmail("outroemail@mail.com");
    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        Usuario usuarioParaSalvar = umUsuario().comId(null).agora();

//        when(repository.getUserByEmail(usuarioParaSalvar.getEmail())).thenReturn(Optional.empty());

        when(repository.salvar(usuarioParaSalvar)).thenReturn(umUsuario().agora());

        Usuario usuarioSalvo = usuarioService.salvar(usuarioParaSalvar);
        assertNotNull(usuarioSalvo.getId());

        verify(repository).getUserByEmail(usuarioParaSalvar.getEmail());
//        verify(repository).salvar(usuarioParaSalvar);
    }

    @Test
    void deveRejeitarUsuarioExistente() {
        Usuario usuario = umUsuario().comId(null).agora();
        when(repository.getUserByEmail(usuario.getEmail())).thenReturn(Optional.of(umUsuario().agora()));

        ValidationException result = assertThrows(ValidationException.class, () -> usuarioService.salvar(usuario));
        assertTrue(result.getMessage().endsWith("já cadastrado!"));

        verify(repository, never()).salvar(usuario);
    }

}
