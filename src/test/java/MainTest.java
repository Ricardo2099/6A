import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MainTest {
    @TempDir Path carpeta;

    @ParameterizedTest
    @CsvSource({"0.20,6", "0.45,15", "0.495,4"})
    void ejecutaCasosDelPdf(String p, String dof) throws Exception {
        Path clases = Path.of(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        Process proceso = new ProcessBuilder(
                Path.of(System.getProperty("java.home"), "bin", "java").toString(),
                "-cp", clases.toString(), "Main", p, dof)
                .directory(carpeta.toFile()).redirectErrorStream(true)
                .redirectOutput(carpeta.resolve("consola.txt").toFile()).start();
        try {
            boolean termino = proceso.waitFor(20, TimeUnit.SECONDS);
            assertTrue(termino && proceso.exitValue() == 0
                    && Files.readString(carpeta.resolve("resultado.txt")).contains("Error absoluto:"));
        } finally {
            if (proceso.isAlive()) proceso.destroyForcibly();
        }
    }
}
