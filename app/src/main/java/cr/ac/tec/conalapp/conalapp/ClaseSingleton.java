package cr.ac.tec.conalapp.conalapp;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.ArrayList;

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

    // IP del servidor
    private static final String URL_HOST = "http://conalapp.000webhostapp.com/";
    /* PHP usuarios */
    public static final String GET_USER_PASS = URL_HOST + "archivosPHP/select_usuario_iniciar_sesion.php";
    public static final String INSERT_USER = URL_HOST + "archivosPHP/persona_insertar.php";

}
