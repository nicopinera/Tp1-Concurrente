import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Reservar extends ReservasDeVuelo {// proceso de reserva
    public static int clientesEnEspera = 0;// usuarios esperando ser atendidos
    private static final Semaphore semaforo = new Semaphore(1);

    @Override
    public void imprimirLista() {// imprime por consola listaAsientos
        for (Asiento a : listaAsientos) {
            System.out.printf("Asiento: %d - %d \n", a.getPos().getFilas(), a.getPos().getColumnas());
        }
    }

    public void run() {// el metodo que corre los 3 hilos
        while (verificarOcupacion()) { // siempre que haya algun lugar desocupado
            Asiento a; // asiento a reservar
            // **********************************************************************************************************
            try { //
                Thread.sleep(240); //
            } catch (InterruptedException e) { // Simula tiempo del proceso
                throw new RuntimeException(e); //
            } //
            // **********************************************************************************************************
            synchronized (Llave.getInstance()) {// sincroniza para no meter hilos si todabia no disminuyo clientes en
                                                // espera en 1
                if (clientesEnEspera > 0) {
                    clientesEnEspera--;
                } else {
                    try {// tipico de vendedor - consumidor siendo generador de usuarios el vendedor
                        Llave.getInstance().wait();// freno el hilo hasta que algun notify lo despierte e intente de
                                                   // vuelta
                        continue;// salta a la siguiente interacion del while principal
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            // **********************************************************************************************************
            try {
                semaforo.acquire();// semaforo para que 2 hilos de reservar no reserven el mismo asiento
                if(!verificarOcupacion()){return;}
                do {
                    a = listaAsientos.get(numeroRamdomAsiento(listaAsientos));// busco asiento siempre que este libre
                } while (!a.getEstado().equals(EstadoAsiento.LIBRE));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {// los finaly se ponen al final de un try y son para ejecutar las lineas si o si
                       // al final o cuuando se produzca exepcion o cuando se salga de la zona en
                       // cuestion
                semaforo.release();// libera el semaforo al final o en caso de excepcion
            }
            // **********************************************************************************************************************************
            a.setEstado(EstadoAsiento.OCUPADO); //
            a.setUsuario(GeneradorUsuarios.getUsuarios().remove(numeroRamdomUsuarios(GeneradorUsuarios.getUsuarios()))); // asigna
                                                                                                                         // el
                                                                                                                         // usuario
                                                                                                                         // al
                                                                                                                         // asiento
            a.setNombreHilo(Thread.currentThread().getName()); //
            // *******************************************************************************************************************************
            try {
                semaforoPendientes.acquire();// semaforo para proteger lista de pendientes
                pendienteDePago.add(a);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                semaforoPendientes.release();
            }
            // **********************************************************************************************************
        }
    }

    private int numeroRamdomUsuarios(ArrayList<Usuario> usuarios) {// manda un indice ramdom de usuario
        Random ran = new Random();
        int aux;
        do {
            aux = ran.nextInt(usuarios.size());
        } while (aux == usuarios.size());
        return aux;
    }
}