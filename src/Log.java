import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log implements Runnable {// log del programa

    private static final Logger LOGGER = Logger.getLogger(Log.class.getName());// variable loger al cual desde cualquier
                                                                               // clase puedo enviar info a este log
    private FileHandler manejador;// para mandar los log a la consola
    {
        try {
            manejador = new FileHandler("log.txt");
            manejador.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Log() {
        LOGGER.setLevel(Level.INFO);// que se mande como info el log
        manejador.setLevel(Level.ALL);// config del manejador
        LOGGER.addHandler(manejador);// pongo el manejador en el loger para que cumpla su funcion
        LOGGER.setUseParentHandlers(false);
    }

    public static Logger getLogger() {
        return LOGGER;
    }// devuelve el logger para que cualquier clase lo pueda usar y es estatico para
     // no nesesitar una instancia de Log

    public void run() {
        // tiempo de ejecucion del programa en segundos
        long tiempo = System.currentTimeMillis();// empieza a correr el tiempo de inicio y lo guardo en tiempo
        while (ReservasDeVuelo.getVerificadas().size() + ReservasDeVuelo.getCanceladas().size() < Avion.getInstance()
                .getCantColumnas() * Avion.getInstance().getCantFilas()) {// siempre que aun no termine el programa
                                                                          // ejecuta esto
            try {
                Thread.sleep(200);// cada 200 milis muestra el log
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("\nReservas Pendientes de pago= " + ReservasDeVuelo.getPendienteDePago().size() + "\n"
                    + "Reservas confirmadas= " + ReservasDeVuelo.getConfirmadas().size() + "\n"
                    + "Reservas Canceladas= " + ReservasDeVuelo.getCanceladas().size() + "\n"
                    + "Reservas Aprobadas= " + ReservasDeVuelo.getVerificadas().size() + "\n"
                    + "Asientos Ocupados= " + getcantOcupados());
            // info del log que muestra por consola
        }
        long resto= (System.currentTimeMillis() - tiempo) % 1000;
        tiempo = (System.currentTimeMillis() - tiempo) / 1000;// tiempo final - inicial dividido en 1000 para que de en
                                                              // segundos
        // muestra por ultima vez la info una vez termine el programa
        LOGGER.info("\nReservas Pendientes de pago= " + ReservasDeVuelo.getPendienteDePago().size() + "\n"
                + "Reservas confirmadas= " + ReservasDeVuelo.getConfirmadas().size() + "\n"
                + "Reservas Canceladas= " + ReservasDeVuelo.getCanceladas().size() + "\n"
                + "Reservas Aprobadas= " + ReservasDeVuelo.getVerificadas().size() + "\n"
                + "Asientos Ocupados= " + getcantOcupados() + "\n"
                + "Tiempo de ejecucion= " + tiempo + " segundos" + " con "+ resto + " millisegundos");// muestra por consola el tiempo de ejecucion
    }

    private String getcantOcupados() {// cantidad de asientos ocupados
        int cantOcupados = 0;
        for (Asiento a : ReservasDeVuelo.getListaAsientos()) {
            if (a.getEstado().equals(EstadoAsiento.OCUPADO)) {
                cantOcupados++;
            }
        }
        return Integer.toString(cantOcupados);
    }
}
