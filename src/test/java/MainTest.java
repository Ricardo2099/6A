import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Test;

public class MainTest {
    @TempDir Path carpeta;

    @Test
    void ejecutaTodosLosCasosSinArgumentos() throws Exception {
        Path clases = Path.of(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        Process proceso = new ProcessBuilder(
                Path.of(System.getProperty("java.home"), "bin", "java").toString(),
                "-cp", clases.toString(), "Main")
                .directory(carpeta.toFile()).redirectErrorStream(true)
                .redirectOutput(carpeta.resolve("consola.txt").toFile()).start();
        try {
            boolean termino = proceso.waitFor(20, TimeUnit.SECONDS);
            assertTrue(termino && proceso.exitValue() == 0);
            String resultado = Files.readString(carpeta.resolve("resultado.txt"));
            assertTrue(resultado.contains("p: 0.2\ndof: 6".replace("\n", System.lineSeparator())));
            assertTrue(resultado.contains("p: 0.45\ndof: 15".replace("\n", System.lineSeparator())));
            assertTrue(resultado.contains("p: 0.495\ndof: 4".replace("\n", System.lineSeparator())));
            assertTrue(Files.readString(carpeta.resolve("consola.txt")).contains(resultado));
        } finally {
            if (proceso.isAlive()) proceso.destroyForcibly();
        }
    }
}
