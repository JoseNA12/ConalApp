package cr.ac.tec.conalapp.conalapp.PantallaComunidades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

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

import cr.ac.tec.conalapp.conalapp.Adaptadores.ListViewAdapterBusquedaComunidades;
import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.Modelo.ComunidadModelo;
import cr.ac.tec.conalapp.conalapp.R;

// https://abhiandroid.com/ui/searchview

public class BusquedaComunidadesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    private ListView lv_busqueda_comunidades;
    private ListViewAdapterBusquedaComunidades adapter;
    private SearchView editsearch;
    private String[] animalNameList;
    private ArrayList<ComunidadModelo> arraylist_comunidades = new ArrayList<ComunidadModelo>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_comunidades);

        initComponentes();

        // Locate the ListView in listview_main.xml

        /*for (int i = 0; i < animalNameList.length; i++) {
            ComunidadModelo animalNames = new ComunidadModelo(animalNameList[i], "", "", "");
            // Binds all strings into an array
            arraylist_comunidades.add(animalNames);
        }

        adapter = new ListViewAdapterBusquedaComunidades(this, arraylist_comunidades);

        lv_busqueda_comunidades.setAdapter(adapter);*/
    }

    private void initComponentes()
    {
        initProgressDialog();
        initListView();
        initSearchView();

        progressDialog.show();
        executeQuery(ClaseSingleton.SELECT_ALL_COMUNIDAD);
    }

    private void initProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
    }

    private void initListView()
    {
        lv_busqueda_comunidades = (ListView) findViewById(R.id.lv_busqueda_comunidades_id);
        lv_busqueda_comunidades.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ComunidadModelo item = (ComunidadModelo) parent.getItemAtPosition(position);
                Log.d(item.getNombre(), "NEPE");
            }
        });
    }

    private void initSearchView()
    {
        editsearch = (SearchView) findViewById(R.id.sv_busqueda_id);
        editsearch.setOnQueryTextListener(this);
    }

    private void obtenerDatosComunidadesResponse(String response){
        arraylist_comunidades = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("false")){
                errorMessageDialog("No ha sido posible cargar los boletines.\nVerifique su conexión a internet!");
            }
            else
            {
                JSONArray jsonArray = new JSONObject(response).getJSONArray("value");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    String provincia = jsonArray.getJSONObject(i).get("Provincia").toString();
                    String canton = jsonArray.getJSONObject(i).get("Canton").toString();
                    String descripcion = jsonArray.getJSONObject(i).get("Descripcion").toString();
                    String nombre =jsonArray.getJSONObject(i).get("Comunidad").toString();
                    String idComunidad = jsonArray.getJSONObject(i).get("IdComunidad").toString();
                    String creador =  jsonArray.getJSONObject(i).get("IdPersonaCreadora").toString();

                    arraylist_comunidades.add(0,
                            new ComunidadModelo(idComunidad, nombre, creador, descripcion, provincia, canton));
                }

                adapter = new ListViewAdapterBusquedaComunidades(this, arraylist_comunidades);

                lv_busqueda_comunidades.setAdapter(adapter);
                lv_busqueda_comunidades.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ComunidadModelo dataModel = arraylist_comunidades.get(position);

                        Intent i =  new Intent(getApplicationContext(), PerfilComunidadActivity.class);
                        i.putExtra("IdComu", dataModel.getIdComunidad());
                        i.putExtra("Nombre", dataModel.getNombre());
                        i.putExtra("Provincia", dataModel.getProvincia());
                        i.putExtra("Canton", dataModel.getCanton());
                        startActivity(i);
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void executeQuery(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        //errorMessageDialog(URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                obtenerDatosComunidadesResponse(response);  /* Para inicio de sesión*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();

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

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
}
