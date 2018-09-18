package cr.ac.tec.conalapp.conalapp.PantallaAgregarInfoPerfil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.Modelo.Persona;
import cr.ac.tec.conalapp.conalapp.PantallaCrearBoletin.CrearBoletinActivity;
import cr.ac.tec.conalapp.conalapp.PantallaLogin.IniciarSesionActivity;
import cr.ac.tec.conalapp.conalapp.PantallaPerfilUsuario.PerfilUsuarioActivity;
import cr.ac.tec.conalapp.conalapp.R;

public class AgregarInfoPerfil extends AppCompatActivity {

    private Button btn_registrar;
    private TextInputEditText ET_nombre;
    private TextInputEditText ET_apellido;
    private TextInputEditText ET_correo;
    private TextInputEditText ET_contrasena;
    private TextInputEditText ET_repetirContrasena;
    private EditText ET_fechaNacimiento;
    private TextInputEditText ET_biografia;
    private TextInputEditText ET_sobrenombre;
    private TextInputEditText ET_lugarResidencia;
    private RadioButton Rbtn_Hombre;

    private DatePickerDialog datePickerDialog;

    private ProgressDialog progressDialog;

    private Persona usuarioActual, nuevoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_informacion_perfil);

         inicializarComponentes();
    }

    private void inicializarComponentes(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Actualizando perfil ...");

        ET_nombre = findViewById(R.id.ET_editarNombre);
        ET_apellido = findViewById(R.id.ET_editarApellido);
        ET_correo = findViewById(R.id.ET_editarCorreo);
        ET_contrasena = findViewById(R.id.ET_editarContrasena);
        ET_repetirContrasena = findViewById(R.id.ET_editarRepContrasena);
        ET_fechaNacimiento = findViewById(R.id.input_fechaNacimiento_id);
        ET_biografia = findViewById(R.id.ET_editarBiografia);
        ET_sobrenombre = findViewById(R.id.ET_editarSobrenombre);
        ET_lugarResidencia = findViewById(R.id.ET_editarLugarResidencia);
        Rbtn_Hombre = findViewById(R.id.radio_hombre);

        btn_registrar = findViewById(R.id.btn_editarRegistro_id);
        btn_registrar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                editarPerfil();
            }

        });

        initCampos();
        initInputFecha();
    }

    private void initCampos(){
        usuarioActual = ClaseSingleton.USUARIO_ACTUAL;

        ET_nombre.setText(usuarioActual.getNombre());
        ET_apellido.setText(usuarioActual.getApellido());
        ET_correo.setText(usuarioActual.getCorreo());
        ET_contrasena.setText("");
        ET_repetirContrasena.setText("");
        ET_biografia.setText(usuarioActual.getBiografia());
        ET_lugarResidencia.setText(usuarioActual.getLugarResidencia());
        ET_sobrenombre.setText(usuarioActual.getSobrenombre());
        Rbtn_Hombre.setChecked(true);
    }

    private void initInputFecha()
    {

        ET_fechaNacimiento.setInputType(InputType.TYPE_NULL); // no mostrar el teclado
        ET_fechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int calendarYear = calendar.get(Calendar.YEAR);
                int calendarMonth = calendar.get(Calendar.MONTH);
                int calendarDay = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AgregarInfoPerfil.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strMonth = String.valueOf(monthOfYear + 1);
                        String strDay = String.valueOf(dayOfMonth);
                        if ((monthOfYear + 1) < 10) strMonth = "0" + strMonth;
                        if (dayOfMonth < 10) strDay = "0" + strDay;
                        ET_fechaNacimiento.setText(year + "-" + strMonth + "-" + strDay);
                    }
                }, calendarYear, calendarMonth, calendarDay);
                datePickerDialog.show();
            }
        });
    }

    private void editarPerfil(){
        nuevoPerfil = new Persona();
        nuevoPerfil.setId(ClaseSingleton.USUARIO_ACTUAL.getId());
        String nombre = ET_nombre.getText().toString();
        String apellido = ET_apellido.getText().toString();
        String correo = ET_correo.getText().toString();
        String lugarResidencia = ET_lugarResidencia.getText().toString();
        String fechaNacimiento = ET_fechaNacimiento.getText().toString();
        String biografia = ET_biografia.getText().toString();
        String sobrenombre = ET_sobrenombre.getText().toString();
        String contrasena = ET_contrasena.getText().toString();
        String repContrasena = ET_repetirContrasena.getText().toString();

        if(nombre.isEmpty()) nuevoPerfil.setNombre(usuarioActual.getNombre());
        else nuevoPerfil.setNombre(nombre);

        if(apellido.isEmpty()) nuevoPerfil.setNombre(usuarioActual.getApellido());
        else nuevoPerfil.setNombre(apellido);

        if(correo.isEmpty()) nuevoPerfil.setNombre(usuarioActual.getCorreo());
        else nuevoPerfil.setNombre(correo);

        if(lugarResidencia.isEmpty()) nuevoPerfil.setNombre(usuarioActual.getLugarResidencia());
        else nuevoPerfil.setNombre(lugarResidencia);

        if(fechaNacimiento.isEmpty()) nuevoPerfil.setNombre(usuarioActual.getFechaNacimiento());
        else nuevoPerfil.setNombre(fechaNacimiento);

        if(biografia.isEmpty()) nuevoPerfil.setNombre(usuarioActual.getBiografia());
        else nuevoPerfil.setNombre(biografia);

        if(sobrenombre.isEmpty()) nuevoPerfil.setNombre(usuarioActual.getSobrenombre());
        else nuevoPerfil.setNombre(sobrenombre);

        if(Rbtn_Hombre.isChecked()) nuevoPerfil.setGenero("Hombre");
        else nuevoPerfil.setGenero("Mujer");

        if (contrasena.isEmpty() || repContrasena.isEmpty()){
            errorMessageDialog("Ingrese su actual contraseña o una nueva, en ambos campos");
        }else if(!contrasena.equals(repContrasena)){
            errorMessageDialog("Error en contraseña.\nVerifique que ambas contraseñas son iguales.");
        }else{
            progressDialog.show();
            executeQuery(ClaseSingleton.UPDATE_USER, contrasena);
        }
    }

    private void executeQuery(String URL, final String contrasena) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // Si serv contesta
                System.out.println(response);
                EditarPerfilResponse(response);
            }
        }, new Response.ErrorListener() {  //Tratar errores conexion con serv
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                errorMessageDialog("No se puede conectar al servidor en estos momentos.\nIntente conectarse más tarde.");
            }
        }) {
            @Override
            protected Map<String, String> getParams() { // Armar Map para enviar al serv mediante un POST
                // System.out.print("get params");
                Map<String, String> params = new HashMap<String, String>();
                params.put("IdPersona", String.valueOf(nuevoPerfil.getId()));
                params.put("Nombre", nuevoPerfil.getNombre());
                params.put("Apellido", nuevoPerfil.getApellido());
                params.put("Correo", nuevoPerfil.getCorreo());
                params.put("Contrasena", contrasena);
                params.put("fechaNacimiento", nuevoPerfil.getFechaNacimiento());
                params.put("biografia", nuevoPerfil.getBiografia());
                params.put("genero", nuevoPerfil.getGenero());
                params.put("lugarResidencia", nuevoPerfil.getLugarResidencia());
                params.put("sobrenombre", nuevoPerfil.getSobrenombre());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
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

    private void EditarPerfilResponse(String response){
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("false")){
                errorMessageDialog("Error al editar perfil.");
            }
            else {
                ClaseSingleton.USUARIO_ACTUAL.setId(jsonObject.getJSONObject("value").getInt("IdPersona"));
                ClaseSingleton.USUARIO_ACTUAL.setCorreo(jsonObject.getJSONObject("value").getString("Correo"));
                ClaseSingleton.USUARIO_ACTUAL.setNombre(jsonObject.getJSONObject("value").getString("Nombre"));
                ClaseSingleton.USUARIO_ACTUAL.setApellido(jsonObject.getJSONObject("value").getString("Apellido"));
                ClaseSingleton.USUARIO_ACTUAL.setFechaNacimiento(jsonObject.getJSONObject("value").getString("FechaNacimiento"));
                ClaseSingleton.USUARIO_ACTUAL.setBiografia(jsonObject.getJSONObject("value").getString("Biografia"));
                ClaseSingleton.USUARIO_ACTUAL.setGenero(jsonObject.getJSONObject("value").getString("Genero"));
                ClaseSingleton.USUARIO_ACTUAL.setLugarResidencia(jsonObject.getJSONObject("value").getString("LugarResidencia"));
                ClaseSingleton.USUARIO_ACTUAL.setSobrenombre(jsonObject.getJSONObject("value").getString("Sobrenombre"));

                Intent intent = new Intent(this, PerfilUsuarioActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
