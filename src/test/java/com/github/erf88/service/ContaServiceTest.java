package com.github.erf88.service;

import com.github.erf88.domain.Conta;
import com.github.erf88.domain.EventType;
import com.github.erf88.domain.builders.ContaBuilder;
import com.github.erf88.domain.exception.ValidationException;
import com.github.erf88.external.ContaEvent;
import com.github.erf88.repository.ContaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.erf88.domain.builders.ContaBuilder.umaConta;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("service")
@Tag("conta")
@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
class ContaServiceTest {

    @InjectMocks private ContaService contaService;
    @Mock private ContaEvent event;
    @Mock private ContaRepository repository;

    @Captor private ArgumentCaptor<Conta> contaCaptor;

    @Test
    void deveSalvarPrimeiraContaComSucesso() throws Exception {
        Conta conta = umaConta().comId(null).agora();
        when(repository.salvar(Mockito.any(Conta.class))).thenReturn(umaConta().agora());
        doNothing().when(event).dispatch(umaConta().agora(), EventType.CREATED);

        Conta result = contaService.salvar(conta);
        assertNotNull(result.getId());

        Mockito.verify(repository).salvar(contaCaptor.capture());
        Assertions.assertNull(contaCaptor.getValue().getId());
        Assertions.assertTrue(contaCaptor.getValue().getNome().startsWith("Conta válida"));
    }

    @Test
    void deveSalvarSegundaContaComSucesso() {
        Conta conta = umaConta().comId(null).agora();
        when(repository.obterContaPorUsuario(conta.getUsuario().getId()))
                .thenReturn(List.of(umaConta().comNome("Outra conta").agora()));
        when(repository.salvar(Mockito.any(Conta.class))).thenReturn(umaConta().agora());

        Conta result = contaService.salvar(conta);
        assertNotNull(result.getId());
    }

    @Test
    void deveRejeitarContaRepetida() {
        Conta conta = umaConta().comId(null).agora();
        when(repository.obterContaPorUsuario(conta.getUsuario().getId())).thenReturn(List.of(umaConta().agora()));
//        when(repository.salvar(conta)).thenReturn(umaConta().agora());

        ValidationException result = assertThrows(ValidationException.class, () -> contaService.salvar(conta));
        assertEquals("Usuário já possui uma conta com este nome", result.getMessage());
    }

    @Test
    void naoDeveManterContaSemEvento() throws Exception {
        Conta conta = umaConta().comId(null).agora();
        Conta contaSalva = umaConta().agora();
        when(repository.salvar(Mockito.any(Conta.class))).thenReturn(contaSalva);
        doThrow(new Exception("Falha catastrófica")).when(event).dispatch(contaSalva, EventType.CREATED);

        Exception result = assertThrows(Exception.class, () -> contaService.salvar(conta));
        assertEquals("Falha na criação da conta, tente novamente", result.getMessage());

        verify(repository).delete(contaSalva);


    }

}