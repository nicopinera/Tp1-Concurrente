public class Asiento {

    private EstadoAsiento estado; // Estado del asiento

    private String nombreHilo;// guarda el nombre del hilo que le asigno un usuario

    private boolean checked = false;// ver si el asiento ya fue checkeado

    private Usuario usuario = null;// usuario que reserva el asiento

    private final Posicion pos;// posicion donde guarda en que fila y columna del asiento

    // Constructor
    public Asiento(int fila, int columna) {

        estado = EstadoAsiento.LIBRE; // empieza libre el asiento

        pos = new Posicion(fila, columna); // asigno posicion del asiento

    }

    // Getter y Setters
    public Posicion getPos() {
        return pos;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getNombreHilo() {
        return nombreHilo;
    }

    public void setNombreHilo(String nombreHilo) {
        this.nombreHilo = nombreHilo;
    }

}
