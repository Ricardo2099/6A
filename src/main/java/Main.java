import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {
    // Editar aqui los pares p y dof que se ejecutaran tambien en Jenkins.
    private record Caso(double p, int dof) { }
    private static final List<Caso> CASOS = List.of(
            new Caso(0.20, 6),
            new Caso(0.45, 15),
            new Caso(0.495, 4));

    public static void main(String[] args) {
        try {
            List<String> lineas = new ArrayList<>();
            for (int i = 0; i < CASOS.size(); i++) {
                Caso caso = CASOS.get(i);
                double p = caso.p();
                int dof = caso.dof();
                double x = Busqueda.buscar(p, dof);
                double integral = Simpson.integrar(x, dof);
                lineas.addAll(List.of(
                    "Caso " + (i + 1),
                    "p: " + p,
                    "dof: " + dof,
                    String.format(Locale.ROOT, "x: %.8f", x),
                    String.format(Locale.ROOT, "Integral de 0 a x: %.8f", integral),
                    String.format(Locale.ROOT, "Error absoluto: %.8f", Math.abs(p - integral)),
                    ""));
            }
            OutFile.escribirResultado("resultado.txt", lineas);
            lineas.forEach(System.out::println);
            System.out.println("Resultado generado en: resultado.txt");
        } catch (IOException | IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
