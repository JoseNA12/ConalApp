package cr.ac.tec.conalapp.conalapp.Modelo;

public class BoletinModelo {

    private String autor; // cambiar luego, manejar objeto usuario
    private String titular;
    private String provincia;
    private String canton;
    private String fecha;
    private String hora;
    private String descripcion;
    private String sospechosos;
    private String armasSosp;
    private String vehiculosSosp;
    private String linkImagenGPS;
    private Persona autorInfo;
    private String idComunidad;

    private String tipoInforme;

    public BoletinModelo(String autor, String titular, String provincia, String canton, String fecha, String hora, String descripcion, String sospechosos, String armasSosp, String vehiculosSosp, String linkImagenGPS, Persona autorInfo, String idComunidad, String pTipoInforme) {
        this.autor = autor;
        this.titular = titular;
        this.provincia = provincia;
        this.canton = canton;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
        this.sospechosos = sospechosos;
        this.armasSosp = armasSosp;
        this.vehiculosSosp = vehiculosSosp;
        this.linkImagenGPS = linkImagenGPS;
        this.autorInfo = autorInfo;
        this.idComunidad = idComunidad;

        this.tipoInforme = pTipoInforme;
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

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
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

    public String getSospechosos() {
        return sospechosos;
    }

    public void setSospechosos(String sospechosos) {
        this.sospechosos = sospechosos;
    }

    public String getArmasSosp() {
        return armasSosp;
    }

    public void setArmasSosp(String armasSosp) {
        this.armasSosp = armasSosp;
    }

    public String getVehiculosSosp() {
        return vehiculosSosp;
    }

    public void setVehiculosSosp(String vehiculosSosp) {
        this.vehiculosSosp = vehiculosSosp;
    }

    public String getLinkImagenGPS() {
        return linkImagenGPS;
    }

    public void setLinkImagenGPS(String linkImagenGPS) {
        this.linkImagenGPS = linkImagenGPS;
    }

    public String getTipoInforme() {
        return tipoInforme;
    }

    public void setTipoInforme(String tipoInforme) {
        this.tipoInforme = tipoInforme;
    }
}
