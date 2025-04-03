import java.util.List;

public class Frase {
    private String texto;
    private List<Traduccion> traducciones;
    private String idioma;

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    // Getters y Setters
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public List<Traduccion> gettraducciones() {
        return traducciones;
    }

    public void settraducciones(List<Traduccion> traducciones) {
        this.traducciones = traducciones;
    }
}


