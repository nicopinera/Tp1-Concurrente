
import java.util.Random;

public class Cancelar extends ReservasDeVuelo {
    public void imprimirLista() {// imprime la lista de asientos cancelados
        for (Asiento a : canceladas) {
            System.out.printf("Asiento: %d - %d \n", a.getPos().getFilas(), a.getPos().getColumnas());
        }
    }

    private boolean verificarCheck() {// devuelve true si algun asiento aun no ha sido checkeado
        for (Asiento s : confirmadas) {
            if (!s.getChecked()) {
                return true;
            }
        }
        return false;
    }
    public void run() {
        Random aux = new Random();// usar numero ramdom
        while (verificarOcupacion() || !pendienteDePago.isEmpty() || verificarCheck()) {// siempre que haya alguna no
                                                                                        // check, pendientes no sea
                                                                                        // vacia o haya algun asiento
                                                                                        // vacio
            try {
                Thread.sleep(120);// tiempo del proceso de cancelar
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Asiento a;// asiento a cancelar o confirmar
            // **********************************************************************************************************
            synchronized (Llave.getInstance4()) {// sincroniza con hilos de cancelar

                try {
                    semaforoConfirmadas.acquire();
                    if (verificarCheck()){
                        do {
                            a = getConfirmadas().get(numeroRamdomAsiento(getConfirmadas()));
                        } while (a.getChecked());
                }
                    else {
                        continue;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    semaforoConfirmadas.release();
                }
                // **********************************************************************************************************
                if (aux.nextInt(9) >= 1) {// 90 por ciento de probabilidad de verificar con check
                    a.setChecked(true);
                } else {// 10 por ciento de cancelar la reserva, remover a de confirmadas y ponerla en
                        // canceladas y quitar el usuario del asiento
                    try {
                        synchronized (Llave.getInstance5()) {// sincroniza con pagar
                            semaforoConfirmadas.acquire();
                            confirmadas.remove(a);
                            semaforoConfirmadas.release();
                            semaforoCanceladas.acquire();
                            canceladas.add(a);
                            semaforoCanceladas.release();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    a.setEstado(EstadoAsiento.DESCARTADO);// poner estado del asiento en descartado
                    a.setUsuario(null);// eliminar usuario
                }
            }

        }
    }
}