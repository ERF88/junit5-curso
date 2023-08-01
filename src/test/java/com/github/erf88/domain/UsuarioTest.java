package com.github.erf88.domain;

import com.github.erf88.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static com.github.erf88.domain.builders.UsuarioBuilder.umUsuario;
import static org.junit.jupiter.api.Assertions.*;

@Tag("dominio")
@Tag("usuario")
public class UsuarioTest {

    @Test
    @DisplayName("Deve criar um usuário válido")
    void deveCriarUsuarioValido() {
        Usuario usuario = umUsuario().agora();

         assertAll("Usuario",
                () -> assertEquals(1L, usuario.getId()),
                () -> assertEquals("Usuário Válido", usuario.getNome()),
                () -> assertEquals("usuario@mail.com", usuario.getEmail()),
                () -> assertEquals("12345678", usuario.getSenha())
        );

    }

    @Test
    @DisplayName("Deve rejeitar um usuário sem nome")
    void deveRejeitarUsuarioSemNome() {
        ValidationException resultado = assertThrows(ValidationException.class,
                () -> umUsuario().comNome(null).agora());
        assertEquals("Nome é obrigatório", resultado.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar um usuário sem email")
    void deveRejeitarUsuarioSemEmail() {
        ValidationException resultado = assertThrows(ValidationException.class,
                () -> umUsuario().comEmail(null).agora());
        assertEquals("Email é obrigatório", resultado.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar um usuário sem senha")
    void deveRejeitarUsuarioSemSenha() {
        ValidationException resultado = assertThrows(ValidationException.class,
                () -> umUsuario().comSenha(null).agora());
        assertEquals("Senha é obrigatória", resultado.getMessage());
    }

//    @ParameterizedTest(name = "[{index}] - {4}")
//    @CsvSource(value = {
//            "1, NULL, usuario@mail.com, 123456, Nome é obrigatório",
//            "1, Nome usuário, NULL, 123456, Email é obrigatório",
//            "1, Nome usuário, usuario@mail.com, NULL, Senha é obrigatória",
//    }, nullValues = "NULL")

//    @ParameterizedTest(name = "[{index}] - {4}")
//    @CsvFileSource(files = "src/test/resources/camposObrigatoriosUsuario.csv", nullValues = "NULL", numLinesToSkip = 1)

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/camposObrigatoriosUsuario.csv", nullValues = "NULL", useHeadersInDisplayName = true)
    void deveValidarCamposObrigatorios(Long id, String nome, String email, String senha, String mensagem) {
        ValidationException resultado = assertThrows(ValidationException.class,
                () -> umUsuario().comId(id).comNome(nome).comEmail(email).comSenha(senha).agora());
        assertEquals(mensagem, resultado.getMessage());
    }

}
