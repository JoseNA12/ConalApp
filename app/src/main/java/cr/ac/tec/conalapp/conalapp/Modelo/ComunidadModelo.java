package cr.ac.tec.conalapp.conalapp.Modelo;

public class ComunidadModelo {

    private String nombre;
    private String descripcion;
    private String provincia;
    private String canton;
    private String creador;
    private String idComunidad;

    public ComunidadModelo(String idComunidad, String nombre, String creador, String descripcion, String provincia, String canton) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.provincia = provincia;
        this.canton = canton;
        this.creador = creador;
        this.idComunidad = idComunidad;
    }

    public String getIdComunidad() {
        return idComunidad;
    }

    public void setIdComunidad(String idComunidad) {
        this.idComunidad = idComunidad;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
}
