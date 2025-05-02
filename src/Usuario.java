public class Usuario {

    private static int id = 0;

    private String nombre;

    public Usuario() {
        id++; // cada vez que se cree un usuario va a tener un nombre diferente y unico
        nombre = "Usuario-" + id;
    }

    // Metodo Getter
    public String getNombre() {
        return nombre;
    }
}