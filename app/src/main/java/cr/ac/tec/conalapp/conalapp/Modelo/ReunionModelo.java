package cr.ac.tec.conalapp.conalapp.Modelo;

public class ReunionModelo {

    private String autor; // cambiar luego, manejar objeto usuario
    private String titular;
    private String provincia; // dividir en distrito
    private String distrito;
    private String fecha;
    private String hora;
    // Imagen GPS
    private String descripcion;


    public ReunionModelo(String autor, String titular, String provincia, String fecha, String hora, String descrpcion) {
        this.autor = autor;
        this.titular = titular;
        this.provincia = provincia;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descrpcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descrpcion) {
        this.descripcion = descrpcion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
}

