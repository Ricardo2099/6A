import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class BusquedaTest {
    @ParameterizedTest
    @CsvSource({"0.20,6", "0.45,15", "0.495,4", "0.000001,9", "0.499,30"})
    void satisfaceElErrorDelPdf(double p, int dof) {
        assertTrue(Math.abs(p - Simpson.integrar(Busqueda.buscar(p, dof), dof)) < 0.00001);
    }

    @ParameterizedTest
    @CsvSource({"0.20,6,0.55338,0.0001", "0.45,15,1.75305,0.0002", "0.495,4,4.60409,0.003"})
    void aproximaLosValoresDeLaTabla(double p, int dof, double esperado, double toleranciaX) {
        // La tolerancia del algoritmo se aplica a p; no garantiza cinco decimales en x.
        assertEquals(esperado, Busqueda.buscar(p, dof), toleranciaX);
    }

    @Test
    void areaCero() {
        assertEquals(0, Busqueda.buscar(0, 6));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, 0.5, 1, Double.NaN, Double.POSITIVE_INFINITY})
    void rechazaProbabilidadInvalida(double p) {
        assertThrows(IllegalArgumentException.class, () -> Busqueda.buscar(p, 6));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 341})
    void rechazaGradosInvalidos(int dof) {
        assertThrows(IllegalArgumentException.class, () -> Busqueda.buscar(0.2, dof));
    }
}
