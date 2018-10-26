package cr.ac.tec.conalapp.conalapp.Modelo;

public enum TipoInforme {

    BOLETIN("Boletín"),
    REUNION("Reunión");

    String nombreInforme;

    TipoInforme(String pNombre){
        nombreInforme = pNombre;
    }

    public String getNombreInforme(){
        return nombreInforme;
    }
}
