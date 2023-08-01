import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CalculadoraTest {

    private Calculadora calc = new Calculadora();

    @BeforeEach
    void setup() {
        System.out.println("--- Before Each ---");
    }

    @AfterEach
    void teardown() {
        System.out.println("--- After Each ---");
    }

    @BeforeAll
    static void setupAll() {
        System.out.println("--- Before All ---");
    }

    @AfterAll
    static void teardownAll() {
        System.out.println("--- After All ---");
    }

    @Test
    void testSomar() {

        Assertions.assertTrue(calc.soma(2, 3) == 5);
        Assertions.assertEquals(5, calc.soma(2, 3));
    }

    @Test
    void deveRetornarNumeroInteiroNaDivisao() {
        float resultado = calc.dividir(6, 2);
        Assertions.assertEquals(3, resultado);
    }

    @Test
    void deveRetornarNumeroNegativoNaDivisao() {
        float resultado = calc.dividir(6, -2);
        Assertions.assertEquals(-3, resultado);
    }

    @Test
    void deveRetornarNumeroDecimalNaDivisao() {
        float resultado = calc.dividir(10, 3);
        Assertions.assertEquals(3.33, resultado, 0.01);
    }

    @Test
    void deveRetornarZeroComNumeradorZeroNaDivisao() {
        float resultado = calc.dividir(0, 2);
        Assertions.assertEquals(0, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoDividirPorZero_Junit4() {
        try {
            float resultado = 10 / 0;
            Assertions.fail("Deveria ter sido lançado uma exceção na divisão.");
        } catch (ArithmeticException e) {
            Assertions.assertEquals("/ by zero", e.getMessage());
        }
    }

    @Test
    void deveLancarExcecaoQuandoDividirPorZero_Junit5() {
        ArithmeticException exception = Assertions.assertThrows(ArithmeticException.class, () -> {
            float resultado = 10 / 0;
        });
        Assertions.assertEquals("/ by zero", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Teste 1", "Teste 2", "Teste 3"})
    void testStrings(String param) {
        System.out.println(param);
        assertNotNull(param);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "6, 2, 3",
            "6, -2, -3",
            "10, 3, 3.3333332538604736",
            "0, 2, 0"
    })
    void deveDividirCorretamente(int num, int den, double res) {
        float resultado = calc.dividir(num, den);
        Assertions.assertEquals(res, resultado);
    }
}
