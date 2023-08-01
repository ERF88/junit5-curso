package com.github.erf88.service;

import com.github.erf88.domain.Conta;
import com.github.erf88.domain.Transacao;
import com.github.erf88.domain.exception.ValidationException;
import com.github.erf88.repository.TransacaoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

import static com.github.erf88.domain.builders.ContaBuilder.umaConta;
import static com.github.erf88.domain.builders.TransacaoBuilder.umaTransacao;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("service")
@Tag("transacao")
//@EnabledIf(value = "isHoraValida")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransacaoServiceTest {

    @InjectMocks
    @Spy
    private TransacaoService service;
    @Mock
    private TransacaoDao dao;
    //    @Mock private ClockService clock;
    @Captor
    private ArgumentCaptor<Transacao> captor;

    @BeforeEach
    void setUp() {
//        when(clock.getCurrentTime()).thenReturn(LocalDateTime.of(LocalDate.now(), LocalTime.of(7, 30, 28)));
        when(service.getTime()).thenReturn(LocalDateTime.of(LocalDate.now(), LocalTime.of(7, 30, 28)));
    }
//    @BeforeEach
//    void setUp() {
//        Assumptions.assumeTrue(LocalDateTime.now().getHour() < 7);
//    }

    @Test
    void deveSalvarTransacaoValida() {
        Transacao transacao = umaTransacao().comId(null).agora();
        when(dao.salvar(transacao)).thenReturn(umaTransacao().agora());


//        LocalDateTime dataDesejada = LocalDateTime.of(LocalDate.now(), LocalTime.of(7, 30, 28));

//        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class)) {
//        try (MockedConstruction<Date> date = Mockito.mockConstruction(Date.class, (mock, context) -> when(mock.getHours()).thenReturn(7))) {
//            ldt.when(LocalDateTime::now).thenReturn(dataDesejada);

        Transacao result = service.salvar(transacao);
        assertEquals(umaTransacao().agora(), result);
        assertAll("Transação",
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Transação Válida", result.getDescricao()),
                () -> {
                    assertAll("Conta",
                            () -> assertEquals("Conta válida", result.getConta().getNome()),
                            () -> {
                                assertAll("Usuário",
                                        () -> assertEquals("Usuário Válido", result.getConta().getUsuario().getNome()),
                                        () -> assertEquals("12345678", result.getConta().getUsuario().getSenha())
                                );
                            }
                    );
                });

//            ldt.verify(LocalDateTime::now);
//            assertEquals(1, date.constructed().size());
//        }

    }

    @ParameterizedTest(name = "{6}")
    @MethodSource(value = "cenariosObrigatorios")
    void deveValidarCamposObrigatoriosAoSalvar(Long id, String descricao, BigDecimal valor, LocalDate data, Conta conta, Boolean status, String message) {

        String result = assertThrows(ValidationException.class, () -> {
            Transacao transacao = umaTransacao()
                    .comId(id)
                    .comDescricao(descricao)
                    .comValor(valor)
                    .comData(data)
                    .comConta(conta)
                    .comStatus(status)
                    .agora();

            service.salvar(transacao);
        }).getMessage();

        assertEquals(message, result);
    }

    static Stream<Arguments> cenariosObrigatorios() {
        return Stream.of(
                Arguments.of(1L, null, BigDecimal.TEN, LocalDate.now(), umaConta().agora(), true, "Descrição inexistente"),
                Arguments.of(1L, "Descrição", null, LocalDate.now(), umaConta().agora(), true, "Valor inexistente"),
                Arguments.of(1L, "Descrição", BigDecimal.TEN, null, umaConta().agora(), true, "Data inexistente"),
                Arguments.of(1L, "Descrição", BigDecimal.TEN, LocalDate.now(), null, true, "Conta inexistente")
        );
    }

//    static boolean isHoraValida() {
//        return LocalDateTime.now().getHour() < 7;
//    }

    @Test
    void deveRejeitarTransacaoSemValor() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Transacao transacao = umaTransacao().comValor(null).agora();

        Method metodo = TransacaoService.class.getDeclaredMethod("validarCamposObrigatorios", Transacao.class);
        metodo.setAccessible(true);

        Exception result = assertThrows(Exception.class, () -> metodo.invoke(new TransacaoService(), transacao));

        assertEquals("Valor inexistente", result.getCause().getMessage());
    }

    @Test
    void deveRejeitarTransacaoTardeDaNoite() {
//        Mockito.reset(service);
        when(service.getTime()).thenReturn(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 30, 28)));

        String result = assertThrows(RuntimeException.class, () -> {
            service.salvar(umaTransacao().agora());
        }).getMessage();

        assertEquals("Tente novamente amanhã", result);
    }

    @Test
    void deveSalvarTransacaoComoPendentePorPadrao() {
        Transacao transacao = umaTransacao().comStatus(null).agora();
        service.salvar(transacao);

        verify(dao).salvar(captor.capture());
        Transacao transacaoValidada = captor.getValue();
        assertFalse(transacaoValidada.getStatus());
    }

}
