package cr.ac.tec.conalapp.conalapp.PantallaComunidades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.Modelo.ReunionModelo;
import cr.ac.tec.conalapp.conalapp.R;

public class CrearComunidadActivity extends AppCompatActivity {

    private EditText input_nombre, input_descripcion;
    private ListView listView;
    private Button btn_crear_comunidad;
    private Spinner sp_provincias, sp_cantones_por_provincia;
    private String promptProvincias = "Seleccione una provincia";
    private String promptCantones = "Seleccione un canton";

    private ProgressDialog progressDialog;

    private String[] provincias, cantones_san_jose, cantones_alajuela, cantones_cartago,
            cantones_guanacaste, cantones_heredia, cantones_puntarenas, cantones_limon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_comunidad);

        inicializarComponentes();
    }

    private void inicializarComponentes()
    {
        initProgressDialog();
        initEditText();
        initButtons();
        initListView();
        initSpinner();
    }

    private void initProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
    }

    private void initEditText()
    {
        input_nombre = (EditText) findViewById(R.id.input_nombre_id);
        input_descripcion = (EditText) findViewById(R.id.input_descripcion_id);
    }

    private void initButtons()
    {
        btn_crear_comunidad = findViewById(R.id.btn_crear_comunidad_id);
        btn_crear_comunidad.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                btn_action_crearComunidad();
            }
        });
    }

    private void initListView()
    {
        listView = (ListView) findViewById(R.id.lv_boletines_id);

        // executeQuery(ClaseSingleton.SELECT_ALL_BOLETIN + "?IdPersona=" + ClaseSingleton.USUARIO_ACTUAL.getId());
    }

    private void initSpinner()
    {
        provincias = agregarOpcion(getResources().getStringArray(R.array.array_provincias_costa_rica), false);

        cantones_san_jose = agregarOpcion(getResources().getStringArray(R.array.array_cantones_san_jose), true);
        cantones_alajuela = agregarOpcion(getResources().getStringArray(R.array.array_cantones_alajuela), true);
        cantones_cartago = agregarOpcion(getResources().getStringArray(R.array.array_cantones_cartago), true);
        cantones_guanacaste = agregarOpcion(getResources().getStringArray(R.array.array_cantones_guanacaste), true);
        cantones_heredia = agregarOpcion(getResources().getStringArray(R.array.array_cantones_heredia), true);
        cantones_puntarenas = agregarOpcion(getResources().getStringArray(R.array.arrar_cantones_puntarenas), true);
        cantones_limon = agregarOpcion(getResources().getStringArray(R.array.array_cantones_limon), true);

        sp_cantones_por_provincia = (Spinner)findViewById(R.id.sp_cantones_id);

        sp_provincias = (Spinner)findViewById(R.id.sp_provincias_id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout, R.id.text, provincias);
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

    private String[] agregarOpcion(String[] pLista, Boolean esCanton)
    {
        List<String> list = new ArrayList<>(Arrays.asList(pLista));

        if (esCanton) { list.add(0, promptCantones); }
        else { list.add(0, promptProvincias); }

        return list.toArray(new String[list.size()]);
    }

    private void btn_action_crearComunidad()
    {
        if (!input_nombre.getText().toString().trim().equals(""))
        {
            String provincia = sp_provincias.getSelectedItem().toString();
            String canton = "";

            try { canton = sp_cantones_por_provincia.getSelectedItem().toString(); }
            catch(NullPointerException e){ canton = ""; }

            if (!provincia.equals(promptProvincias) && canton.equals(promptCantones))
            {
                MessageDialog("Por favor, seleccione un canton.");
            }
            else
            {
                if (provincia.equals(promptProvincias)) // No quizo meter provincia y cantón
                {
                    provincia = ""; canton = "";
                }
                executeQuery(ClaseSingleton.INSERT_COMUNIDAD, input_nombre.getText().toString(),
                        input_descripcion.getText().toString(), provincia, canton);
            }
        }
        else
        {
            MessageDialog("Por favor, ingrese un nombre para la comunidad.");
        }
    }

    private void executeQuery(String URL, final String nombre, final String descripcion, final String provincia,
                              final String canton) {

        progressDialog = ProgressDialog.show(this,"Atención","Creando comunidad...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // Si serv contesta
                // System.out.println(response);
                RegistrarComunidad_Response(response);
            }
        }, new Response.ErrorListener() {  //Tratar errores conexion con serv
            @Override
            public void onErrorResponse(VolleyError error) {
                //MessageDialog(error.toString());
                progressDialog.dismiss();
                MessageDialog("No se puede conectar al servidor en estos momentos.\nIntente conectarse más tarde.");
            }
        }) {
            @Override
            protected Map<String, String> getParams() { // Armar Map para enviar al serv mediante un POST
                //System.out.print("get params");
                Map<String, String> params = new HashMap<String, String>();
                params.put("IdPersona", String.valueOf(ClaseSingleton.USUARIO_ACTUAL.getId()));
                params.put("Nombre", nombre);
                params.put("Descripcion", descripcion);
                params.put("Provincia", provincia);
                params.put("Canton", canton);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void RegistrarComunidad_Response(String response){
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("false")){
                MessageDialog("No se pudo crear la comunidad. Inténtelo de nuevo o más tarde.");
            }
            else {
                //MessageDialog("Se ha creado la comunidad correctamente.");
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void MessageDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                //      .setIcon(R.drawable.ic_img_diag_error_icon)
                .setMessage(message).setTitle("Atención")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
