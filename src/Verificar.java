import java.util.concurrent.Semaphore;
import java.util.logging.Level;
public class Verificar extends ReservasDeVuelo{
    private static boolean aux=true;
    private static final Semaphore semaforo= new Semaphore(1);
    public void run() {
        while (verificarOcupacion() || !pendienteDePago.isEmpty() || !confirmadas.isEmpty()) {//siempre que haya lugares sin ocupar o pendientes no este vacia o confirmads no este vacia
            Asiento a;//asiento a verificar
            try {
                Thread.sleep(300);// tiempo del proceso de verificar
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //**********************************************************************************************************
            synchronized (Llave.getInstance2()) {//sincronizar para hilos de verificar
                try {
                    semaforoConfirmadas.acquire();
                    if (verificarCheck()) {//si confirmada no esta vacia
                        do {
                            a = confirmadas.get(numeroRamdomAsiento(confirmadas));// un asiento random de confirmadas
                        } while (!a.getChecked()); // a es null para que entre la primera vez, o a no este chequeada, entonces asigna un nuevo a hasta que sea válido
                    } else {
                        continue;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    semaforoConfirmadas.release();
                }
//**********************************************************************************************************
                try {
                    verificadas.add(a);//si lo anterior no tiro errores al log entonces agrega a verificadas el asiento
                    semaforoConfirmadas.acquire();
                    confirmadas.remove(a);//saca de confirmadas al asiento
                    semaforoConfirmadas.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
//**********************************************************************************************************
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//**********************************************************************************************************
        try{
        semaforo.acquire();//semaforo para evitar que impriman varios hilos
            if (!aux){return;}
            aux=false;
        for (Asiento a : listaAsientos) {
            try {
                validacion(a);
            } catch (Exception e) {
                Log.getLogger().log(Level.SEVERE, e.getMessage());//si validar tiro excepcion entonces lo infroma al log
                System.exit(0);
            }
        }
        Log.getLogger().log(Level.INFO, "funcionamiento sin problemas");
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }finally {
            semaforo.release();
        }
    }
    private void validacion(Asiento a) {//tira errores con mensajes que seran llevados al log si no coinciden las listas
     if (a.getEstado().equals(EstadoAsiento.LIBRE )){
         throw new IllegalStateException("Hay asientos libres ");
     }
     if (a.getUsuario()==null && !a.getEstado().equals(EstadoAsiento.DESCARTADO)){
         throw new IllegalStateException("asiento verificado sin usuario");
     }
     if (pendienteDePago.contains(a) ){
         throw new IllegalStateException("Asiento contenido en Pendientes");
     }
        if (confirmadas.contains(a)){
            throw new IllegalStateException("Asiento contenido en confirmadas");
        }
        if (!a.getChecked() && !a.getEstado().equals(EstadoAsiento.DESCARTADO)){
            throw new IllegalStateException("asiento no checkeado");
        }
    }
    private boolean verificarCheck() {// devuelve true si algun asiento aun no ha sido checkeado
        for (Asiento s : confirmadas) {
            if (s.getChecked()) {
                return true;
            }
        }
        return false;
    }
    public void imprimirLista() {// imprime lista de verificadas
        for (Asiento a : verificadas) {
            System.out.printf("Asiento: %d - %d \n", a.getPos().getFilas(), a.getPos().getColumnas());
        }

    }
}
