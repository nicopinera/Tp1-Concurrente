import java.util.ArrayList;

public class GeneradorUsuarios implements Runnable {// simula usuarios
    private static final ArrayList<Usuario> usuarios = new ArrayList<>();// usuarios que genera y pasa a reservar

    public static ArrayList<Usuario> getUsuarios() {// get lista de usuarios que genera el generador de usurios
        return usuarios;
    }

    public void run() { // el metodo que corre el hilo
        while (!ReservasDeVuelo.lleno) {// siempre que no esten todos los asientos ocupados, genera usuarios en cola
            try {
                Thread.sleep(140);// pausa el hilo, lo udo psts dimulsr tiempo
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (Llave.getInstance()) {// seccion critica
                if (Reservar.clientesEnEspera < ReservasDeVuelo.listaAsientos.size()) { // Verifica si hay asientos
                                                                                        // disponibles
                    usuarios.add(new Usuario());// genera el usuario para enviar a reservar
                    Reservar.clientesEnEspera++;// agrega usuario a la colaa
                }
                if (Reservar.clientesEnEspera >= ReservasDeVuelo.listaAsientos.size()) { // Verifica si el avión está
                                                                                         // lleno
                    ReservasDeVuelo.lleno = true;// avisa que esta lleno para no mandar mas usuarios a reservar
                }
                Llave.getInstance().notify();// despierta a algun hilo dormido de reservar
            }
        }
    }
}