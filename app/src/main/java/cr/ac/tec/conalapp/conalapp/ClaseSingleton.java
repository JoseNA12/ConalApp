package cr.ac.tec.conalapp.conalapp;

import cr.ac.tec.conalapp.conalapp.Modelo.Persona;

public class ClaseSingleton {


    private static ClaseSingleton instancia;

    private ClaseSingleton(){}

    public static synchronized ClaseSingleton getInstance(){
        if(instancia == null){
            instancia = new ClaseSingleton();
        }
        return instancia;
    }

    public static final String archContactosSalud = "contactos_salud.txt";
    public static final String archContactosSeguridad = "contactos_seguridad.txt";

    public static final String nSan_Jose = "San José";
    public static final String nAlajuela = "Alajuela";
    public static final String nCartago = "Cartago";
    public static final String nHeredia = "Heredia";
    public static final String nGuanacaste = "Guanacaste";
    public static final String nPuntarenas = "Puntarenas";
    public static final String nLimon = "Limón";


    public static final String linkImagenGPSNoDisponible = "https://firebasestorage.googleapis.com/v0/b/conalapp-74fc6.appspot.com/o/images%2Fimagen_gps_no_disponible.jpg?alt=media&token=54d7d602-087a-402f-a688-257e5195c93d";

    // -------------------------------------------------------------------------------- //

    /**
     * Gmail: conalapp@gmail.com
     * Passw: conalapptec123
     *
     * 000webhost: conalapp@gmail.com
     * Passw: conalapptec123
     *
     * BaseDatos: conalappbd
     * Passw: conalapptec123
     */

    // Usuario Actual
    public static Persona USUARIO_ACTUAL = new Persona();

    // IP del servidor
    private static final String URL_HOST = "http://conalapp.000webhostapp.com/";

    /* PHP usuarios */
    public static final String GET_USER_PASS = URL_HOST + "archivosPHP/select_usuario_iniciar_sesion.php";
    public static final String INSERT_USER = URL_HOST + "archivosPHP/persona_insertar.php";
    public static  final String SELECT_USER_BY_ID = URL_HOST + "archivosPHP/select_usuario_by_id.php";
    public static final String UPDATE_USER = URL_HOST + "archivosPHP/persona_update.php";
    public static  final String DELETE_USER = URL_HOST + "archivosPHP/persona_delete.php";

    // PHP Boletin
    public static final String INSERT_BOLETIN = URL_HOST + "archivosPHP/boletin_insertar.php";
    public static final String SELECT_ALL_BOLETIN = URL_HOST + "archivosPHP/boletin_select_all.php";

    // PHP Reunion
    public static final String INSERT_REUNION = URL_HOST + "archivosPHP/reunion_insertar.php";
    public static final String SELECT_ALL_REUNION = URL_HOST + "archivosPHP/reunion_select_all.php";

    // PHP Puntos Interes
    public static final String INSERT_PUNTO_INTERES = URL_HOST + "archivosPHP/puntosInteres_insertar.php";
    public static final String SELECT_PUNTO_INTERES_BY_USUARIO = URL_HOST + "archivosPHP/puntosInteres_select_by_id.php";
    public static final String DELETE_PUNTO_INTERES_BY_ID = URL_HOST + "archivosPHP/puntosInteres_delete_by_id.php";

    // PHP Comunidades
    public static final String INSERT_COMUNIDAD = URL_HOST + "archivosPHP/.php"; // TODO: Meter el nombre del php
    // Request con todas las comunidades por haber (select *)
    public static final String SELECT_ALL_COMUNIDAD = URL_HOST + "archivosPHP/.php";
    // Request de todas las comunidades por haber, unicamente el nombre (select nombre), mas la cantidad de boletines asociados
    public static final String SELECT_ALL_COMUNIDAD_WITH_COUNT_BOLETINES = URL_HOST + "archivosPHP/.php";
    // devuelve la cantidad de boletines asociados a una comunidad, recibe el ID de la comunidad
    public static final String SELECT_ALL_COUNT_BOLETINES_BY_ID = URL_HOST + "archivosPHP/.php"; // el ID es de la comunidad
    // devuelve la cantidad de reuniones asociados a una comunidad, recibe el ID de la comunidad
    public static final String SELECT_ALL_COUNT_REUNIONES_BY_ID = URL_HOST + "archivosPHP/.php"; // el ID es de la comunidad

}
