import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public abstract class ReservasDeVuelo implements Runnable {
    /*
     * clase que guarda todas las variables estaticas que hacen falta
     * Las demas clases extienden de esta para poder manejar sus variables
     */

    // en un principio no se le asigna avion
    protected static Avion avion = null;

    // indica si ya estan todos los asientos ocupados en la lista listaAsientos
    protected static boolean lleno = false;

    // asientos del avion(este array no se modifica nunca)
    protected static ArrayList<Asiento> listaAsientos = new ArrayList<>();

    // cuando pasan el proceso de reserva,se agregan a esta lista
    protected static ArrayList<Asiento> pendienteDePago = new ArrayList<>();

    // una parte luego de pagar, confirman y se borran de la lista de pendientes y
    // se los agrega a esta
    protected static ArrayList<Asiento> confirmadas = new ArrayList<>();

    // hay una probablidad que algunos procesos cancelen y en tal caso , se agregan
    // a esta lista
    protected static ArrayList<Asiento> canceladas = new ArrayList<>();

    // lista de verificados
    protected static ArrayList<Asiento> verificadas = new ArrayList<>();

    protected static Semaphore semaforoPendientes = new Semaphore(1);

    protected static Semaphore semaforoCanceladas = new Semaphore(1);

    protected static Semaphore semaforoConfirmadas = new Semaphore(1);

    // constructor que solo la primera vez que se crea, arma la lista de asientos
    public ReservasDeVuelo() {

        if (avion == null) {

            // construye la listaAsientos
            generarAsientos(Avion.getInstance());

        }
    }

    // metodo abstracto para hacer que la clase sea abstracta y no instanciable
    public abstract void imprimirLista();

    // genera los asientos y los agrega a la lista (se hace por unica vez)
    public void generarAsientos(Avion a) {

        avion = a;

        for (int i = 1; i <= a.getCantFilas(); i++) {

            for (int j = 1; j <= a.getCantColumnas(); j++) {

                listaAsientos.add(new Asiento(i, j));

            }
        }
    }

    public int numeroRamdomAsiento(ArrayList<Asiento> lista) {
        /*
         * busca un numero random entre 0 y el size de asientos,
         * pero hay que aclarar que como indice el ultimo numero no sirve, por eso le
         * resto uno cuando ocurre dicho caso
         */

        Random ran = new Random();

        int aux;

        do {

            aux = ran.nextInt(lista.size());

        } while (aux == lista.size());

        return aux;
    }

    public boolean verificarOcupacion() {
        // devuelve true si al menos hay un asiento libre, si no encuentra, pone lleno
        // en false
        for (Asiento s : listaAsientos) {

            if (s.getEstado().equals(EstadoAsiento.LIBRE)) {

                return true;

            }
        }

        lleno = true;

        return false;
    }



    // Getters
    public static ArrayList<Asiento> getCanceladas() {
        return canceladas;
    }

    public static ArrayList<Asiento> getConfirmadas() {
        return confirmadas;
    }

    public static ArrayList<Asiento> getListaAsientos() {
        return listaAsientos;
    }

    public static ArrayList<Asiento> getPendienteDePago() {
        return pendienteDePago;
    }

    public static ArrayList<Asiento> getVerificadas() {
        return verificadas;
    }
}