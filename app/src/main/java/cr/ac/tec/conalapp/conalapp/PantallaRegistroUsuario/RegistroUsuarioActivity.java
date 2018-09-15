package cr.ac.tec.conalapp.conalapp.PantallaRegistroUsuario;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.PantallaLogin.IniciarSesionActivity;
import cr.ac.tec.conalapp.conalapp.PantallaPrincipal.PrincipalActivity;
import cr.ac.tec.conalapp.conalapp.R;

public class RegistroUsuarioActivity extends AppCompatActivity {
    private TextInputEditText ET_usuarioCorreo;
    private TextInputEditText ET_usuarioContrasena;
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
            progressDialog.show();
            executeQuery(ClaseSingleton.INSERT_USER + "?Nombre=" +usuarioNombre + "&Apellido="+ usuarioApellido+ "&Correo=" + usuarioCorreo + "&Contrasena=" + usuarioContrasena);


        }
    }

    private void executeQuery(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
       // errorMessageDialog(URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                RegistrarUsuarioResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                errorMessageDialog("No se puede conectar al servidor en estos momentos.\nIntente conectarse más tarde.");
                // errorMessageDialog(error.toString());
            }
        });queue.add(stringRequest);
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

