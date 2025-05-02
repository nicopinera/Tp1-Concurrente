public class Posicion {

    private int fila, columna;// Fila y columna del asiento

    // Constructor
    public Posicion(int fila, int columna) {

        this.fila = fila;

        this.columna = columna;

    }

    // Getters
    public int getColumnas() {
        return columna;
    }

    public int getFilas() {
        return fila;
    }
}