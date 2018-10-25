package cr.ac.tec.conalapp.conalapp.Modelo;

public class ReunionModelo {

    private String autor; // cambiar luego, manejar objeto usuario
    private String titular;
    private String provincia;
    private String canton;
    private String distrito;
    private String fecha;
    private String hora;
    private String descripcion;
    private String linkImagenGPS;
    private Persona autorInfo;
    private String idComunidad;

    public ReunionModelo(String autor, String titular, String provincia, String fecha, String hora, String linkImagenGPS, String descrpcion, String canton, Persona autorInfo, String idComunidad) {
        this.autor = autor;
        this.titular = titular;
        this.provincia = provincia;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descrpcion;
        this.linkImagenGPS = linkImagenGPS;
        this.canton = canton;
        this.autorInfo = autorInfo;
        this.idComunidad = idComunidad;
    }

    public String getIdComunidad() {
        return idComunidad;
    }

    public void setIdComunidad(String idComunidad) {
        this.idComunidad = idComunidad;
    }

    public Persona getAutorInfo() {
        return autorInfo;
    }

    public void setAutorInfo(Persona autorInfo) {
        this.autorInfo = autorInfo;
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

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
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

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLinkImagenGPS() {
        return linkImagenGPS;
    }

    public void setLinkImagenGPS(String linkImagenGPS) {
        this.linkImagenGPS = linkImagenGPS;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }
}

