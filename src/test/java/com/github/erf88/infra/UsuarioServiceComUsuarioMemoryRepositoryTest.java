package com.github.erf88.infra;

import com.github.erf88.domain.Usuario;
import com.github.erf88.domain.exception.ValidationException;
import com.github.erf88.service.UsuarioService;
import org.junit.jupiter.api.*;

import static com.github.erf88.domain.builders.UsuarioBuilder.umUsuario;
import static org.junit.jupiter.api.Assertions.*;

@Tag("infra")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioServiceComUsuarioMemoryRepositoryTest {

    private UsuarioService usuarioService = new UsuarioService(new UsuarioMemoryRepository());

    @Test
    @Order(1)
    public void deveSalvarUsuarioValido() {
        Usuario usuario = usuarioService.salvar(umUsuario().comId(null).agora());

        assertNotNull(usuario.getId());
    }

    @Test
    @Order(2)
    public void deveRejeitarUsuarioExistente() {
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioService.salvar(umUsuario().comId(null).comEmail("user@mail.com").agora()));

        assertEquals("Usuário user@mail.com já cadastrado!", ex.getMessage());
    }

}
