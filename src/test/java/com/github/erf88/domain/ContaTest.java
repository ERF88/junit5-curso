package com.github.erf88.domain;

import com.github.erf88.domain.builders.ContaBuilder;
import com.github.erf88.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.erf88.domain.builders.UsuarioBuilder.umUsuario;
import static org.junit.jupiter.api.Assertions.*;

@Tag("dominio")
@Tag("conta")
public class ContaTest {

    @Test
    @DisplayName("Deve criar uma conta Válida")
    void deveCriarUmaContaValida() {
        Conta conta = ContaBuilder.umaConta().agora();

        assertAll("Conta",
                () -> assertEquals(1L, conta.getId()),
                () -> assertEquals("Conta válida", conta.getNome()),
                () -> assertEquals(umUsuario().agora(), conta.getUsuario())
        );
    }

    @ParameterizedTest
    @MethodSource(value = "dataProvider")
    @DisplayName("Deve Rejeitar uma conta inválida")
    void deveRejeitarUmaContaInvalida(Long id, String nome, Usuario usuario, String mensagem) {
        String resultado = assertThrows(ValidationException.class,
                () -> ContaBuilder.umaConta().comId(id).comNome(nome).comUsuario(usuario).agora()).getMessage();
        assertEquals(mensagem, resultado);
    }

    private static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of(1L, null, umUsuario().agora(), "Nome é obrigatório"),
                Arguments.of(1L, "Conta válida", null, "Usuário é obrigatório")
        );
    }
}
