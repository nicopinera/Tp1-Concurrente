import java.util.Random;

public class Pagar extends ReservasDeVuelo {
    public void imprimirLista() {
        for (Asiento a : pendienteDePago) {
            System.out.printf("Asiento: %d - %d \n", a.getPos().getFilas(), a.getPos().getColumnas());
        }
    }

    public void run() {
        Random prob = new Random();
        Asiento a;
        while (verificarOcupacion() || !pendienteDePago.isEmpty()) {// siempre que haya lugares sin reservar o haya
                                                                    // pendientes
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // **********************************************************************************************************
            synchronized (Llave.getInstance3()) {// sincroniza los hilos de pagar
                try {
                    semaforoPendientes.acquire();
                    if (!pendienteDePago.isEmpty()) {
                        a = pendienteDePago.get(numeroRamdomAsiento(pendienteDePago));
                    } else {
                        continue;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    semaforoPendientes.release();
                }
                // **********************************************************************************************************
                try {
                    synchronized (Llave.getInstance5()) {// sincroniza con cancelar
                        semaforoPendientes.acquire();
                        pendienteDePago.remove(a);
                        semaforoPendientes.release();

                        if (prob.nextInt(9) >= 1) {
                            semaforoConfirmadas.acquire();
                            confirmadas.add(a);
                            semaforoConfirmadas.release();
                        } else {
                            semaforoCanceladas.acquire();
                            canceladas.add(a);
                            semaforoCanceladas.release();
                            a.setEstado(EstadoAsiento.DESCARTADO);
                            a.setUsuario(null);
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}