package cr.ac.tec.conalapp.conalapp;


public class ContactosModelo {

    String nombre_centro;
    String nombre_provincia;
    String numero_telefono;
    String feature;

    public ContactosModelo(String nombre_centro, String nombre_provincia, String numero_telefono, String feature ) {
        this.nombre_centro = nombre_centro;
        this.nombre_provincia = nombre_provincia;
        this.numero_telefono = numero_telefono;
        this.feature = feature;
    }

    public String getNombreCentro() {
        return nombre_centro;
    }

    public String getType() {
        return nombre_provincia;
    }

    public String getNumeroTelefono() {
        return numero_telefono;
    }

    public String getFeature() {
        return feature;
    }

}

