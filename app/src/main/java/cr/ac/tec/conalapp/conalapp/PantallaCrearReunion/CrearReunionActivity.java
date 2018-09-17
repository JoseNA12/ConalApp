package cr.ac.tec.conalapp.conalapp.PantallaCrearReunion;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

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
import cr.ac.tec.conalapp.conalapp.PantallaCrearBoletin.CrearBoletinActivity;
import cr.ac.tec.conalapp.conalapp.R;

public class CrearReunionActivity extends AppCompatActivity {

    private TextInputEditText input_titular, input_proposito;
    private EditText input_fecha, input_hora;
    private Button btn_crear_reunion;

    private String[] provincias, cantones_san_jose, cantones_alajuela, cantones_cartago,
            cantones_guanacaste, cantones_heredia, cantones_puntarenas, cantones_limon;

    private DatePickerDialog datePickerDialog;

    private Spinner sp_provincias, sp_cantones_por_provincia;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reunion);

        inicializarComponentes();
    }

    private void inicializarComponentes()
    {
        input_titular = (TextInputEditText) findViewById(R.id.input_titular_id);
        initInputFecha();
        initInputHora();
        initSpinners();
        input_proposito = (TextInputEditText) findViewById(R.id.input_proposito_id);
        btn_crear_reunion = (Button) findViewById(R.id.btn_crear_reunion_id);
        btn_crear_reunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarReunion();
            }
        });

    }

    private void initInputFecha()
    {
        input_fecha = (EditText) findViewById(R.id.input_fecha_id);
        input_fecha.setInputType(InputType.TYPE_NULL); // no mostrar el teclado
        input_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int calendarYear = calendar.get(Calendar.YEAR);
                int calendarMonth = calendar.get(Calendar.MONTH);
                int calendarDay = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(CrearReunionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strMonth = String.valueOf(monthOfYear + 1);
                        String strDay = String.valueOf(dayOfMonth);
                        if ((monthOfYear + 1) < 10) strMonth = "0" + strMonth;
                        if (dayOfMonth < 10) strDay = "0" + strDay;
                        input_fecha.setText(year + "-" + strMonth + "-" + strDay);
                    }
                }, calendarYear, calendarMonth, calendarDay);
                datePickerDialog.show();
            }
        });
    }

    private void initInputHora()
    {
        input_hora = (EditText) findViewById(R.id.input_hora_id);
        input_hora.setInputType(InputType.TYPE_NULL); // no mostrar el teclado
        input_hora.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CrearReunionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        input_hora.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.show();

            }
        });
    }

    private void initSpinners()
    {
        provincias = getResources().getStringArray(R.array.array_provincias_costa_rica);
        cantones_san_jose = getResources().getStringArray(R.array.array_cantones_san_jose);
        cantones_alajuela = getResources().getStringArray(R.array.array_cantones_alajuela);
        cantones_cartago = getResources().getStringArray(R.array.array_cantones_cartago);
        cantones_guanacaste = getResources().getStringArray(R.array.array_cantones_guanacaste);
        cantones_heredia = getResources().getStringArray(R.array.array_cantones_heredia);
        cantones_puntarenas = getResources().getStringArray(R.array.arrar_cantones_puntarenas);
        cantones_limon = getResources().getStringArray(R.array.array_cantones_limon);

        sp_cantones_por_provincia = (Spinner)findViewById(R.id.sp_cantones_id);

        sp_provincias = (Spinner)findViewById(R.id.sp_provincias_id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, provincias);
        sp_provincias.setAdapter(adapter);

        sp_provincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                switch (sp_provincias.getSelectedItem().toString())
                {
                    case ClaseSingleton.nSan_Jose: // <- string
                        establecerAdaptadorSPCantones(cantones_san_jose);
                        break;

                    case ClaseSingleton.nAlajuela:
                        establecerAdaptadorSPCantones(cantones_alajuela);
                        break;

                    case ClaseSingleton.nCartago:
                        establecerAdaptadorSPCantones(cantones_cartago);
                        break;

                    case ClaseSingleton.nHeredia:
                        establecerAdaptadorSPCantones(cantones_heredia);
                        break;

                    case ClaseSingleton.nGuanacaste:
                        establecerAdaptadorSPCantones(cantones_guanacaste);
                        break;

                    case ClaseSingleton.nPuntarenas:
                        establecerAdaptadorSPCantones(cantones_puntarenas);
                        break;

                    case ClaseSingleton.nLimon:
                        establecerAdaptadorSPCantones(cantones_limon);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void establecerAdaptadorSPCantones(String[] pLista)
    {
        ArrayAdapter<String> adapter_cant = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, pLista);
        sp_cantones_por_provincia.setAdapter(adapter_cant);
    }

    private void publicarReunion(){

        if (!input_titular.getText().toString().trim().equals(""))
        {
            if (!input_hora.getText().toString().equals("") && !input_fecha.getText().toString().equals(""))
            {
                executeQuery(ClaseSingleton.INSERT_REUNION, String.valueOf(ClaseSingleton.USUARIO_ACTUAL.getId()), sp_provincias.getSelectedItem().toString(),
                        sp_cantones_por_provincia.getSelectedItem().toString(), input_titular.getText().toString(),
                        input_proposito.getText().toString(), input_hora.getText().toString(), input_fecha.getText().toString(), "EnlaceGPS");
            }
            else
            {
                MessageDialog("Debe ingresar una fecha y hora del acontecimiento!");
            }
        }
        else
        {
            MessageDialog("Debe ingresar un titular!");
        }


    }
    private void executeQuery(String URL, final String IdPersona, final String Provincia, final String Canton, final String Titular, final String Descripcion, final String Hora, final String Fecha, final String EnlaceGPS) {

        progressDialog = ProgressDialog.show(this,
                "Atención",
                "Publicando reunión...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // Si serv contesta
                System.out.println(response);
                RegistrarReunion_Response(response);
            }
        }, new Response.ErrorListener() {  //Tratar errores conexion con serv
            @Override
            public void onErrorResponse(VolleyError error) {
                //MessageDialog(error.toString());
                MessageDialog("No se puede conectar al servidor en estos momentos.\nIntente conectarse más tarde.");
            }
        }) {
            @Override
            protected Map<String, String> getParams() { // Armar Map para enviar al serv mediante un POST
                System.out.print("get params");
                Map<String, String> params = new HashMap<String, String>();
                params.put("IdPersona", IdPersona);
                params.put("Titular", Titular);
                params.put("Provincia", Provincia);
                params.put("Canton", Canton);
                params.put("Fecha", Fecha);
                params.put("Hora", Hora);
                params.put("Descripcion", Descripcion);
                params.put("EnlaceGPS", EnlaceGPS);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void RegistrarReunion_Response(String response){
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("false")){
                MessageDialog("No se pudo publicar la reunión. Inténtelo de nuevo.");
            }
            else {
                MessageDialog("Se ha publicado la reunión correctamente.");
                //Intent intent = new Intent();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void MessageDialog(String message){ // mostrar mensaje emergente
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(message).setTitle("Atención!").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
