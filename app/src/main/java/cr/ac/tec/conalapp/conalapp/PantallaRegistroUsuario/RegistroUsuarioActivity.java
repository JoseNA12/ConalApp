package cr.ac.tec.conalapp.conalapp.PantallaRegistroUsuario;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.PantallaLogin.IniciarSesionActivity;
import cr.ac.tec.conalapp.conalapp.PantallaPrincipal.PrincipalActivity;
import cr.ac.tec.conalapp.conalapp.R;

public class RegistroUsuarioActivity extends AppCompatActivity {
    private TextInputEditText ET_usuarioCorreo;
    private TextInputEditText ET_usuarioContrasena, ET_usuarioContrasenaRepetir;
    private TextInputEditText ET_usuarioNombre;
    private TextInputEditText ET_usuarioApellido;

    private Button btn_registrar;

    private ProgressDialog progressDialog;

    private String usuarioCorreo;
    private String usuarioContrasena;
    private String usuarioNombre;
    private String usuarioApellido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        inicializarComponentes();
    }

    private void inicializarComponentes()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario ...");

        ET_usuarioCorreo = findViewById(R.id.ET_usuarioCorreo);
        ET_usuarioContrasena = findViewById(R.id.ET_usuarioContrasena);
        ET_usuarioContrasenaRepetir = findViewById(R.id.ET_usuarioContrasenaREP);
        ET_usuarioApellido = findViewById(R.id.ET_usuarioApellido);
        ET_usuarioNombre = findViewById(R.id.ET_usuarioNombre);
        btn_registrar = findViewById(R.id.btn_registrar_id);

        btn_registrar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                registrarUsuario();
            }

        });
    }


    private void registrarUsuario(){

        usuarioCorreo = ET_usuarioCorreo.getText().toString();
        usuarioContrasena = ET_usuarioContrasena.getText().toString();
        usuarioApellido = ET_usuarioApellido.getText().toString();
        usuarioNombre = ET_usuarioNombre.getText().toString();

        /* VALIDACIÓN: las entradas no deben ser vacías */
        if (usuarioCorreo.equals("") || usuarioContrasena.equals("")) {
            errorMessageDialog("Error campos vacios");

        }else {

            if (ET_usuarioContrasenaRepetir.getText().toString().equals(ET_usuarioContrasena.getText().toString()))
            {
                progressDialog.show();
                executeQuery(ClaseSingleton.INSERT_USER, usuarioNombre, usuarioApellido, usuarioCorreo, usuarioContrasena);
            }
           else
            {
                errorMessageDialog("Las contraseñas no coinciden!");
            }
        }
    }

    private void executeQuery(String URL, final String nombre, final String apellido, final String correo, final String contrasena) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // Si serv contesta
                System.out.println(response);
                RegistrarUsuarioResponse(response);
            }
        }, new Response.ErrorListener() {  //Tratar errores conexion con serv
            @Override
            public void onErrorResponse(VolleyError error) {
                errorMessageDialog(error.toString());
                errorMessageDialog("No se puede conectar al servidor en estos momentos.\nIntente conectarse más tarde.");
            }
        }) {
            @Override
            protected Map<String, String> getParams() { // Armar Map para enviar al serv mediante un POST
                System.out.print("get params");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Nombre", nombre);
                params.put("Apellido", apellido);
                params.put("Correo", correo);
                params.put("Contrasena", contrasena);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void RegistrarUsuarioResponse(String response){
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("false")){
                errorMessageDialog("Error al registrar usuario.\nEl correo ingresado se encuentra registrado en la plataforma.");
            }
            else {
                //correctMessageDialog("Se ha registrado correctamente.");
                Intent intent = new Intent(this, IniciarSesionActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void errorMessageDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                //      .setIcon(R.drawable.ic_img_diag_error_icon)
                .setMessage(message).setTitle("Error")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void correctMessageDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
               // .setIcon(R.drawable.ic_img_diag_check_icon)
                .setMessage(message)
                .setTitle("Ejecución Correcta")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}

