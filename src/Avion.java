public class Avion {
    private final int cantFilas, cantColumnas;// Cantidad de filas y columnas del Avion
    private static Avion a = null;

    // Constructor del Avion - Privado
    private Avion(int filas, int columnas) {

        cantColumnas = columnas;

        cantFilas = filas;

    }

    // Establecer la unica Intancia de avion
    public static void setInstance(int f, int c) {
        if (a == null) {
            a = new Avion(f, c);
        }
    }

    // Devuelve la unica instancia de Avion
    public static Avion getInstance() {
        if (a != null) {
            return a;
        } throw new IllegalStateException();

    }

    // Getters de cantidad de filas y columnas
    public int getCantColumnas() {
        return cantColumnas;
    }

    public int getCantFilas() {
        return cantFilas;
    }
}
