package cr.ac.tec.conalapp.conalapp.Modelo;

public class Persona {
    private int id;
    private String correo;
    private String nombre;
    private String apellido;
    private String sobrenombre;
    private String lugarResidencia;
    private String fechaNacimiento;
    private String biografia;
    private String genero;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getSobrenombre() {
        return sobrenombre;
    }

    public void setSobrenombre(String sobrenombre) {
        if (sobrenombre.equals("null")){
            this.sobrenombre = "Sin definir";
        }else{
            this.sobrenombre = sobrenombre;
        }
    }

    public String getLugarResidencia() {
        return lugarResidencia;
    }

    public void setLugarResidencia(String lugarResidencia) {
        if (lugarResidencia.equals("null")){
            this.lugarResidencia = "Sin definir";
        }else{
            this.lugarResidencia = lugarResidencia;
        }
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        if (fechaNacimiento.equals("null")){
            this.fechaNacimiento = "Sin definir";
        }else{
            this.fechaNacimiento = fechaNacimiento;
        }
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        if (biografia.equals("null")){
            this.biografia = "Sin definir";
        }else{
            this.biografia = biografia;
        }

    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        if (genero.equals("null")){
            this.genero = "Sin definir";
        }else{
            this.genero = genero;
        }
    }
}
