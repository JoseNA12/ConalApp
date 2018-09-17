package cr.ac.tec.conalapp.conalapp.Modelo;

public class PuntosInteresModelo {

    private String idPreferencia, idUsuario, provincia, canton;

    public PuntosInteresModelo(String idPreferencia, String idUsuario, String provincia, String canton) {
        this.idPreferencia = idPreferencia;
        this.idUsuario = idUsuario;
        this.provincia = provincia;
        this.canton = canton;
    }

    public String getIdPreferencia() {
        return idPreferencia;
    }

    public void setIdPreferencia(String idPreferencia) {
        this.idPreferencia = idPreferencia;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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
