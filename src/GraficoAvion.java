import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GraficoAvion extends JFrame implements ActionListener, Runnable {
    // no hace faltta explicar , no es evalueable, parte grafica
    private final HashMap<Asiento, JButton> mapaAsientos = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public GraficoAvion() {
        setTitle(
                "Asientos del avion   -   gris=Asiento libre   -   Amarillo= Pendiente de pago   -   Azul=Confirmado   -   Rojo=Cancelado   -   Verde=Verificado");
        generarGrafico(Avion.getInstance());
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagenes/Avion.jpg")));

        pack(); // Ajustar el tamaño del JFrame para que se ajuste a los componentes
        setLocationRelativeTo(null); // Centrar el JFrame en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar el JFrame
    }

    private void generarGrafico(Avion a) {
        JPanel panelBotones = new JPanel(new GridLayout(a.getCantFilas(), a.getCantColumnas(), 10, 10)); // Panel para
                                                                                                         // los botones
                                                                                                         // con
                                                                                                         // GridLayout

        for (Asiento c : ReservasDeVuelo.getListaAsientos()) {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(30, 30)); // Establecer el tamaño preferido del botón
            b.setBackground(new Color(102, 94, 94));
            panelBotones.add(b);
            b.addActionListener(this);
            mapaAsientos.put(c, b);
        }

        setLayout(new BorderLayout()); // Establecer el administrador de diseño del JFrame
        add(panelBotones, BorderLayout.CENTER); // Añadir el panel de botones al centro del JFrame
    }

    public void actionPerformed(ActionEvent e) {
        for (Asiento b : mapaAsientos.keySet()) {
            if (e.getSource() == mapaAsientos.get(b)) {
                JFrame ventanasec = new JFrame("Asiento");
                ventanasec.setIconImage(
                        Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagenes/Avion.jpg")));
                ventanasec.setBounds(550, 225, 300, 200);
                JButton v = new JButton(b.getPos().getFilas() + "-" + b.getPos().getColumnas());
                v.setVisible(true);
                v.setBounds(0, 0, 300, 200);
                v.setBackground(mapaAsientos.get(b).getBackground());
                v.setFont(new Font("Arial", Font.PLAIN, 40));
                ventanasec.add(v);
                v.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ventanasec.setVisible(false);
                        JFrame ventana3 = new JFrame("Info Asiento");
                        ventana3.setIconImage(
                                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagenes/Avion.jpg")));
                        ventana3.setBounds(550, 225, 340, 200);
                        JLabel j;
                        if (b.getUsuario() == null) {
                            j = new JLabel(
                                    "<html>" + "Asiento= " + b.getPos().getFilas() + "-" + b.getPos().getColumnas()
                                            + "<br>Estado= " + b.getEstado() + "<br>Usuario= Sin Usuario" + "<br>");

                        } else {
                            j = new JLabel("<html>" + "Asiento= " + b.getPos().getFilas() + "-"
                                    + b.getPos().getColumnas() + "<br>Estado= " + b.getEstado() + "<br>Usuario= "
                                    + b.getUsuario().getNombre() + "<br>");

                        }
                        if (ReservasDeVuelo.getConfirmadas().contains(b)) {
                            j.setText(j.getText() + "Condicion=Esperando aprobacion" + "<br>" + "Hilo que lo reservo="
                                    + b.getNombreHilo() + "</html>");
                        } else if (ReservasDeVuelo.getCanceladas().contains(b)) {
                            j.setText(j.getText() + "Condicion=Reserva cancelada" + "<br>" + "Hilo que lo reservo="
                                    + b.getNombreHilo() + "</html>");
                        } else if (ReservasDeVuelo.getVerificadas().contains(b)) {
                            j.setText(j.getText() + "Condicion=Reserva aprobada" + "<br>" + "Hilo que lo reservo="
                                    + b.getNombreHilo() + "</html>");
                        } else {
                            j.setText(j.getText() + "Condicion=Sin reserva" + "</html>");
                        }

                        j.setVisible(true);
                        j.setBounds(0, 0, 300, 200);
                        j.setBackground(mapaAsientos.get(b).getBackground());
                        j.setFont(new Font("Arial", Font.PLAIN, 20));
                        j.setHorizontalAlignment(SwingConstants.CENTER);
                        j.setVerticalAlignment(SwingConstants.TOP);
                        ventana3.add(j);
                        ventana3.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent e) {
                                setVisible(true);
                            }
                        });
                        ventana3.setVisible(true);
                        setVisible(false);
                    }
                });

                ventanasec.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        setVisible(true);
                    }
                });

                ventanasec.setVisible(true);
                setVisible(false);
                break; // Termina el bucle después de encontrar el botón correspondiente
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            SwingUtilities.invokeLater(() -> {
                lock.writeLock().lock();
                if (!ReservasDeVuelo.getConfirmadas().isEmpty()) {
                    for (Asiento as : ReservasDeVuelo.getConfirmadas()) {
                        try {
                            mapaAsientos.get(as).setBackground(new Color(16, 85, 175, 255));
                        } catch (Exception e) {
                        }

                    }
                }

                if (!ReservasDeVuelo.getCanceladas().isEmpty()) {
                    for (Asiento as : ReservasDeVuelo.getCanceladas()) {
                        try {
                            mapaAsientos.get(as).setBackground(new Color(255, 0, 0, 255));
                        } catch (Exception e) {
                        }

                    }
                }

                if (!ReservasDeVuelo.getPendienteDePago().isEmpty()) {
                    for (Asiento as : ReservasDeVuelo.getPendienteDePago()) {
                        try {
                            mapaAsientos.get(as).setBackground(new Color(219, 253, 0, 255));
                        } catch (Exception e) {
                        }

                    }
                }

                if (!ReservasDeVuelo.getVerificadas().isEmpty()) {
                    for (Asiento as : ReservasDeVuelo.getVerificadas()) {
                        try {
                            mapaAsientos.get(as).setBackground(new Color(2, 173, 37, 255));
                        } catch (Exception e) {
                        }

                    }
                }
                lock.writeLock().unlock();

            });
        }
    }
}
