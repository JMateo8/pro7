public class ObjetoErroneo extends Exception {

    
    // private static final long serialVersionUID = 1L;
    public ObjetoErroneo(String texto) {
        super(texto);
    }

    public void mensaje(String texto) {
        System.err.println("Objeto con datos erroneos en " + texto); //Si lado <=0 salta excepcion  y aparece este mensaje
    }
}
