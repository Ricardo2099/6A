import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Uso: java -cp target/classes Main <p> <dof>");
            System.exit(1);
        }
        try {
            double p = Double.parseDouble(args[0]);
            int dof = Integer.parseInt(args[1]);
            double x = Busqueda.buscar(p, dof);
            List<String> lineas = List.of(
                    "p: " + p,
                    "dof: " + dof,
                    String.format(Locale.ROOT, "x: %.8f", x),
                    String.format(Locale.ROOT, "Integral de 0 a x: %.8f", Simpson.integrar(x, dof)),
                    String.format(Locale.ROOT, "Error absoluto: %.8f", Math.abs(p - Simpson.integrar(x, dof))));
            OutFile.escribirResultado("resultado.txt", lineas);
            lineas.forEach(System.out::println);
            System.out.println("Resultado generado en: resultado.txt");
        } catch (IOException | IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
