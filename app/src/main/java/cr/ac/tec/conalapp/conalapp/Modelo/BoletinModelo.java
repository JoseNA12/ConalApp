package cr.ac.tec.conalapp.conalapp.Modelo;

public class BoletinModelo {

    private String autor; // cambiar luego, manejar objeto usuario
    private String titular;
    private String provincia; // dividir en distrito
    private String fecha;
    private String hora;
    // Imagen GPS
    private String descrpcion;


    public BoletinModelo(String autor, String titular, String provincia, String fecha, String hora, String descrpcion) {
        this.autor = autor;
        this.titular = titular;
        this.provincia = provincia;
        this.fecha = fecha;
        this.hora = hora;
        this.descrpcion = descrpcion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescrpcion() {
        return descrpcion;
    }

    public void setDescrpcion(String descrpcion) {
        this.descrpcion = descrpcion;
    }
}
