public final class Busqueda {
    public static final double TOLERANCIA = 0.00001;
    private static final int MAX_ITERACIONES = 100_000;

    private Busqueda() { }

    public static double buscar(double p, int dof) {
        Simpson.validarDatos(0, dof);
        if (!Double.isFinite(p) || p < 0 || p >= 0.5) {
            throw new IllegalArgumentException("p debe ser finito y cumplir 0 <= p < 0.5.");
        }
        if (p == 0) {
            return 0;
        }
        double x = 1.0;
        double paso = 0.5;
        int signoAnterior = 0;
        for (int i = 0; i < MAX_ITERACIONES; i++) {
            double error = p - Simpson.integrar(x, dof);
            if (Math.abs(error) < TOLERANCIA) {
                return x;
            }
            int signo = error > 0 ? 1 : -1;
            if (signoAnterior != 0 && signo != signoAnterior) {
                paso /= 2;
            }
            double siguiente = x + signo * paso;
            if (siguiente == x) {
                break;
            }
            x = siguiente;
            signoAnterior = signo;
        }
        throw new IllegalStateException("La busqueda no alcanzo la tolerancia con el limite de iteraciones.");
    }
}
