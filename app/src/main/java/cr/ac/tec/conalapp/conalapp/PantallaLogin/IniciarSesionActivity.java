package cr.ac.tec.conalapp.conalapp.PantallaLogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.PantallaPrincipal.PrincipalActivity;
import cr.ac.tec.conalapp.conalapp.PantallaRegistroUsuario.RegistroUsuarioActivity;
import cr.ac.tec.conalapp.conalapp.R;

public class IniciarSesionActivity extends AppCompatActivity {

    private Button btn_ingresar;
    private TextView tv_crear_cuenta;
    private EditText ET_usuarioCorreo;
    private EditText ET_usuarioContrasena;

    private ProgressDialog progressDialog;
    private String usuarioCorreo;
    private String usuarioContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_iniciar_sesion);

        inicializarComponentes();
    }

    private void inicializarComponentes()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión ...");

        ET_usuarioCorreo = findViewById(R.id.input_email_id);
        ET_usuarioContrasena = findViewById(R.id.input_contrasenia_id);
        btn_ingresar = findViewById(R.id.btn_ingresar_id);


        btn_ingresar.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               iniciarSesion();
           }

        });

        tv_crear_cuenta = findViewById(R.id.crear_cuenta_id);
        tv_crear_cuenta.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                goto_resgistrarUsuario();
            }

        });
    }

    private void iniciarSesion()
    {
        usuarioCorreo = ET_usuarioCorreo.getText().toString();
        usuarioContrasena = ET_usuarioContrasena.getText().toString();

        /* VALIDACIÓN: las entradas no deben ser vacías */
        if (usuarioCorreo.trim().equals("") || usuarioContrasena.trim().equals("")) {
            errorMessageDialog("Error campos vacios");

        }else {
            progressDialog.show();
            executeQuery(ClaseSingleton.GET_USER_PASS + "?Correo=" + usuarioCorreo + "&Contrasena=" + usuarioContrasena);
        }
    }

    private void iniciarSesionResponse(String response){
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("false")){
                errorMessageDialog("No se pudo iniciar sesión.\nVerifique que el usuario que ingresó exista, que la contraseña sea la correcta, y que sea usuario activo");
            }
            else {
                ClaseSingleton.USUARIO_ACTUAL.setId(jsonObject.getJSONObject("value").getInt("IdPersona"));
                ClaseSingleton.USUARIO_ACTUAL.setCorreo(jsonObject.getJSONObject("value").getString("Correo"));
                ClaseSingleton.USUARIO_ACTUAL.setNombre(jsonObject.getJSONObject("value").getString("Nombre"));
                ClaseSingleton.USUARIO_ACTUAL.setApellido(jsonObject.getJSONObject("value").getString("Apellido"));
                ClaseSingleton.USUARIO_ACTUAL.setFechaNacimiento(jsonObject.getJSONObject("value").getString("fechaNacimiento"));
                ClaseSingleton.USUARIO_ACTUAL.setBiografia(jsonObject.getJSONObject("value").getString("biografia"));
                ClaseSingleton.USUARIO_ACTUAL.setGenero(jsonObject.getJSONObject("value").getString("genero"));
                ClaseSingleton.USUARIO_ACTUAL.setLugarResidencia(jsonObject.getJSONObject("value").getString("lugarResidencia"));
                ClaseSingleton.USUARIO_ACTUAL.setSobrenombre(jsonObject.getJSONObject("value").getString("sobrenombre"));

                Intent intent = new Intent(this, PrincipalActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void goto_resgistrarUsuario()
    {
        Intent intent = new Intent(this, RegistroUsuarioActivity.class);
        startActivity(intent);
    }

    private void executeQuery(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        //errorMessageDialog(URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                iniciarSesionResponse(response);  /* Para inicio de sesión*/

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
}
