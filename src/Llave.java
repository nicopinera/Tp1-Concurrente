public class Llave { // llaves
    private static final Llave l1 = new Llave();
    private static final Llave l2 = new Llave();
    private static final Llave l3 = new Llave();
    private static final Llave L4 = new Llave();
    private static final Llave L5 = new Llave();

    private Llave() {
    }

    public static Llave getInstance() {
        return l1;
    }

    public static Llave getInstance2() {
        return l2;
    }

    public static Llave getInstance3() {
        return l3;
    }

    public static Llave getInstance4() {
        return L4;
    }

    public static Llave getInstance5() {
        return L5;
    }
}
