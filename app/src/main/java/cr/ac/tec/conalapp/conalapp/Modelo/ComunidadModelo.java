package cr.ac.tec.conalapp.conalapp.Modelo;

public class ComunidadModelo {

    private String nombre;
    private String descripcion;
    private String provincia;
    private String canton;

    public ComunidadModelo(String nombre, String descripcion, String provincia, String canton) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.provincia = provincia;
        this.canton = canton;
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
