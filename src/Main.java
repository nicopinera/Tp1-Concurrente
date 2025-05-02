import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) {
        final Avion avion;

        generarAvion(); // Generar Avions

        Thread generador = new Thread(new GeneradorUsuarios()); // Hilo que genera simula usuarios llegando

        ArrayList<Thread> hilos = new ArrayList<>(); // Todos los hilos de la agencia

        Thread log = new Thread(new Log());// hilo que corre el log

        generarhilosVerificar(hilos); // Generar Hilos para Proceso de Verificacion

        generarHilosReserva(hilos); // Generar Hilos para Proceso de Reserva

        generarHilosPagar(hilos); // Generar Hilos Para Proceso De Pago

        generarHilosCancelar(hilos); // Generar Hilos Para Proceso de Cancelacion

        iniciarHilos(hilos); // Inicializacion de Hilos

        generador.start(); // Corro hilo generador de usuarios

        log.start();// corro hilo log

        {
            GraficoAvion ga = new GraficoAvion();

            ga.setBounds(0, 40, 1350, 450);

            ga.setVisible(true);

            ga.setResizable(false);

            // creo hilo interfaz grafica
            Thread grafico = new Thread(ga);

            // corro hilo interfaz grafica
            grafico.start();
            
        } // parte grafica
        try {
            log.join();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String s="";
        int aux=0;
        for (Thread t: hilos) {
            aux++;
            s+="\n"+"Estado de hilo"+aux+": "+t.getState();
        }
        Log.getLogger().log(Level.INFO,s);
    }



    public static void generarhilosVerificar(ArrayList<Thread> hilos) {
        for (int i = 0; i < 2; i++) {
            hilos.add(new Thread(new Verificar()));
        }
    }

    public static void generarHilosReserva(ArrayList<Thread> hilos) {
        for (int i = 0; i < 3; i++) {
            hilos.add(new Thread(new Reservar()));
        }
    }

    public static void generarHilosPagar(ArrayList<Thread> hilos) {
        for (int i = 0; i < 2; i++) {
            hilos.add(new Thread(new Pagar()));
        }
    }

    public static void generarHilosCancelar(ArrayList<Thread> hilos) {
        for (int i = 0; i < 3; i++) {
            hilos.add(new Thread(new Cancelar()));
        }
    }

    public static void iniciarHilos(ArrayList<Thread> hilos) {
        for (Thread t : hilos) {
            t.start();
        }
    }

    public static void generarAvion() {

        int cantFilas = 0, cantColumnas = 0;

        Scanner scanner = new Scanner(System.in);

        String confirmacion;

        System.out.println("Quiere usar las filas y columnas por defecto (6x31), responder con si o con no: ");

        confirmacion = scanner.nextLine();

        while (!confirmacion.toLowerCase().trim().equals("si") && !confirmacion.toLowerCase().trim().equals("no")) {

            System.out.println("Responda con si o con no");

            confirmacion = scanner.nextLine();

        }

        if (confirmacion.toLowerCase().trim().equals("si")) {

            Avion.setInstance(6, 31); // Inicializo el avion con las filas y columnas de asientos

        } else {

            do {

                try {

                    System.out.println("ingrese la cantidad de filas y columnas del avion\n filas:");

                    cantFilas = scanner.nextInt();

                    System.out.println("columnas:");

                    cantColumnas = scanner.nextInt();

                    System.out.println("ingrese numeros mayores a 0");

                } catch (InputMismatchException e) {

                    System.out.println("ingrese solo numeros");

                    scanner.next();
                }
            } while (cantColumnas <= 0 || cantFilas <= 0);

            Avion.setInstance(cantFilas, cantColumnas);

        }
    }
}