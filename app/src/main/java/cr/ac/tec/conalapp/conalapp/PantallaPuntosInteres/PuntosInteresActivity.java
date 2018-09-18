package cr.ac.tec.conalapp.conalapp.PantallaPuntosInteres;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cr.ac.tec.conalapp.conalapp.Adaptadores.ListViewAdapterPuntosInteres;
import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.Modelo.PuntosInteresModelo;
import cr.ac.tec.conalapp.conalapp.PantallaPrincipal.PrincipalActivity;
import cr.ac.tec.conalapp.conalapp.R;

public class PuntosInteresActivity extends AppCompatActivity {

    private Spinner sp_provincias, sp_cantones_por_provincia;
    private String[] provincias, cantones_san_jose, cantones_alajuela, cantones_cartago,
            cantones_guanacaste, cantones_heredia, cantones_puntarenas, cantones_limon;

    private Button btn_agregar_punto;

    private ListView lv_mis_puntos_interes;
    private ArrayList<PuntosInteresModelo> array_puntos_interes;
    private static ListViewAdapterPuntosInteres adapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_interes);
        inicializarComponentes();
    }

    private void inicializarComponentes()
    {
        provincias = getResources().getStringArray(R.array.array_provincias_costa_rica);
        cantones_san_jose = getResources().getStringArray(R.array.array_cantones_san_jose);
        cantones_alajuela = getResources().getStringArray(R.array.array_cantones_alajuela);
        cantones_cartago = getResources().getStringArray(R.array.array_cantones_cartago);
        cantones_guanacaste = getResources().getStringArray(R.array.array_cantones_guanacaste);
        cantones_heredia = getResources().getStringArray(R.array.array_cantones_heredia);
        cantones_puntarenas = getResources().getStringArray(R.array.arrar_cantones_puntarenas);
        cantones_limon = getResources().getStringArray(R.array.array_cantones_limon);

        initSpinners();

        btn_agregar_punto = (Button) findViewById(R.id.btn_agregar_punto_id);
        btn_agregar_punto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                agregarUbicacion();
            }

        });

        lv_mis_puntos_interes = (ListView) findViewById(R.id.lv_mis_puntos_interes_id);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando información ...");
        progressDialog.show();
        executeQueryCargarPuntosInteres(ClaseSingleton.SELECT_PUNTO_INTERES_BY_USUARIO
                + "?IdPersona=" + ClaseSingleton.USUARIO_ACTUAL.getId());
    }

    private void initSpinners()
    {
        sp_cantones_por_provincia = (Spinner)findViewById(R.id.sp_cantones_id);

        sp_provincias = (Spinner)findViewById(R.id.sp_provincias_id);
        ArrayAdapter<String> adapterSpiner = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, provincias);
        sp_provincias.setAdapter(adapterSpiner);

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

    private void agregarUbicacion(){

        String provincia = sp_provincias.getSelectedItem().toString();
        String canton = sp_cantones_por_provincia.getSelectedItem().toString();

        progressDialog.setTitle("Enviando información ...");
        progressDialog.show();
        executeQuery(ClaseSingleton.INSERT_PUNTO_INTERES,String.valueOf(ClaseSingleton.USUARIO_ACTUAL.getId()), provincia,canton);

    }

    private void executeQuery(String URL, final String idusuario, final String provincia, final String canton) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // Si serv contesta
                // System.out.println(response);
                agregarUbicacionResponse(response);
            }
        }, new Response.ErrorListener() {  //Tratar errores conexion con serv
            @Override
            public void onErrorResponse(VolleyError error) {
                // errorMessageDialog(error.toString());
                progressDialog.dismiss();
                errorMessageDialog("No se puede conectar al servidor en estos momentos.\nIntente conectarse más tarde.");
            }
        }) {
            @Override
            protected Map<String, String> getParams() { // Armar Map para enviar al serv mediante un POST
                System.out.print("get params");
                Map<String, String> params = new HashMap<String, String>();
                params.put("IdUsuario", idusuario);
                params.put("Provincia", provincia);
                params.put("Canton", canton);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void agregarUbicacionResponse(String response){  // Tratar respuesta

        try{
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("false")){
                progressDialog.dismiss();
                errorMessageDialog("El punto de interés seleccionado ya se encuentra asociado.");
            }
            else {
                infoMessageDialog("Se ha agregado el punto de interés correctamente.");
                progressDialog.setTitle("Solicitando información ...");
                progressDialog.show();
                executeQueryCargarPuntosInteres(ClaseSingleton.SELECT_PUNTO_INTERES_BY_USUARIO
                        + "?IdPersona=" + ClaseSingleton.USUARIO_ACTUAL.getId()); // recargar la vista
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void obtenerDatosPuntosInteresResponse(String response){
        array_puntos_interes = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("false")){
                errorMessageDialog("No ha sido posible cargar los puntos de interés.\nVerifique su conexión a internet!");
            }
            else
            {
                JSONArray jsonArray = new JSONObject(response).getJSONArray("value");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    String idPreferencia = jsonArray.getJSONObject(i).get("IdPreferencia").toString();
                    String idUsuario = jsonArray.getJSONObject(i).get("IdUsuario").toString();
                    String provincia = jsonArray.getJSONObject(i).get("Provincia").toString();
                    String canton = jsonArray.getJSONObject(i).get("Canton").toString();

                    array_puntos_interes.add(
                            new PuntosInteresModelo(idPreferencia, idUsuario, provincia, canton));
                }

                adapter = new ListViewAdapterPuntosInteres(array_puntos_interes, this);

                lv_mis_puntos_interes.setAdapter(adapter);
                lv_mis_puntos_interes.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        PuntosInteresModelo dataModel = array_puntos_interes.get(position);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void executeQueryCargarPuntosInteres(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        //errorMessageDialog(URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                obtenerDatosPuntosInteresResponse(response);  /* Para inicio de sesión*/

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

    private void infoMessageDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                //.setIcon(R.drawable.ic_img_diag_info_icon)
                .setMessage(message).setTitle("Información")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    return;
                }
            });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
