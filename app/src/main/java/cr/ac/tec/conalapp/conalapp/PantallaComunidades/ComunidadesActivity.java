package cr.ac.tec.conalapp.conalapp.PantallaComunidades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

import cr.ac.tec.conalapp.conalapp.Adaptadores.ListViewAdapterComunidadInforme;
import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.Modelo.BoletinModelo;
import cr.ac.tec.conalapp.conalapp.Modelo.Persona;
import cr.ac.tec.conalapp.conalapp.Modelo.TipoInforme;
import cr.ac.tec.conalapp.conalapp.R;

// https://www.youtube.com/watch?v=rCmF2Ie1m0Y

public class ComunidadesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeLayout;
    private ArrayList<BoletinModelo> array_informes_comunidades;
    private ListView listView;
    private static ListViewAdapterComunidadInforme adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunidades);

        inicializarComponentes();
    }

    private void inicializarComponentes()
    {
        initProgressDialog();
        initSwipeLayout();
        initListView();
    }

    private void initProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
    }

    private void initSwipeLayout()
    {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_blue_dark);
    }

    private void initListView()
    {
        listView = (ListView) findViewById(R.id.lv_comunidades_id);

        progressDialog.setMessage("Cargando boletines..."); progressDialog.show();
        progressDialog.show();
        executeQuery_getBoletines(ClaseSingleton.SELECT_ALL_COUNT_BOLETINES_BY_ID + "?IdPersona=" + ClaseSingleton.USUARIO_ACTUAL.getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle preses on the actiob bar items
        switch (item.getItemId())
        {
            case R.id.action_crear_comunidad:
                startActivity(new Intent(getApplicationContext(), CrearComunidadActivity.class));
                return true;

            case R.id.action_buscar_comunidad:
                startActivity(new Intent(getApplicationContext(), BusquedaComunidadesActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_action_bar_comunidades, menu);
        return true;
    }

    public void onRefresh() {
        executeQuery_getBoletines(ClaseSingleton.SELECT_ALL_COUNT_BOLETINES_BY_ID + "?IdPersona=" + ClaseSingleton.USUARIO_ACTUAL.getId());
         // SELECT_ALL_COUNT_REUNIONES_BY_ID
    }

    private void obtenerDatosBoletinesResponse(String response){
        array_informes_comunidades = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("false")){
                errorMessageDialog("No ha sido posible cargar los boletines.\nVerifique su conexión a internet!");
            }
            else
            {
                Log.d("PEPA", response);

                if (!jsonObject.getString("status").equals("no hay comunidades"))
                {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String titular = jsonArray.getJSONObject(i).get("Titular").toString();
                        String provincia = jsonArray.getJSONObject(i).get("Provincia").toString();
                        String canton = jsonArray.getJSONObject(i).get("Canton").toString();
                        String fecha = jsonArray.getJSONObject(i).get("Fecha").toString();
                        String hora = jsonArray.getJSONObject(i).get("Hora").toString();
                        String descripcion = jsonArray.getJSONObject(i).get("Descripcion").toString();
                        String sospechosos = jsonArray.getJSONObject(i).get("Sospechosos").toString();
                        String armasSosp = jsonArray.getJSONObject(i).get("ArmasSosp").toString();
                        String vehiculosSosp = jsonArray.getJSONObject(i).get("VehiculosSosp").toString();
                        String linkImagenGPS = jsonArray.getJSONObject(i).get("EnlaceGPS").toString();
                        String nombreComu = jsonArray.getJSONObject(i).get("Comunidad").toString();

                        // Info usuario
                        String idAutor = jsonArray.getJSONObject(i).get("IdPersona").toString();
                        String nombreAutor = jsonArray.getJSONObject(i).get("Nombre").toString();
                        String apellidoAutor = jsonArray.getJSONObject(i).get("Apellido").toString();
                        String correoAutor = jsonArray.getJSONObject(i).get("Correo").toString();
                        String sobrenombreAutor = jsonArray.getJSONObject(i).get("sobrenombre").toString();
                        String lugarResidencia = jsonArray.getJSONObject(i).get("lugarResidencia").toString();
                        String generoAutor = jsonArray.getJSONObject(i).get("genero").toString();
                        String fechaNacimiento = jsonArray.getJSONObject(i).get("fechaNacimiento").toString();
                        String biografia = jsonArray.getJSONObject(i).get("biografia").toString();

                        Persona autor = new Persona();
                        autor.setId(Integer.valueOf(idAutor));
                        autor.setCorreo(correoAutor);
                        autor.setNombre(nombreAutor);
                        autor.setApellido(apellidoAutor);
                        autor.setFechaNacimiento(fechaNacimiento);
                        autor.setBiografia(biografia);
                        autor.setGenero(generoAutor);
                        autor.setLugarResidencia(lugarResidencia);
                        autor.setSobrenombre(sobrenombreAutor);

                        //System.out.println("Autor " + autor.getNombre());
                        //System.out.println("Autor " + autor.getId());
                        array_informes_comunidades.add(i,
                                new BoletinModelo(autor.getNombre() + autor.getApellido(), titular, nombreComu, canton, fecha, hora, descripcion,
                                        sospechosos, armasSosp, vehiculosSosp, linkImagenGPS, autor, nombreComu, TipoInforme.BOLETIN.getNombreInforme()));

                        executeQuery_getReuniones(ClaseSingleton.SELECT_ALL_COUNT_REUNIONES_BY_ID + "?IdPersona=" + ClaseSingleton.USUARIO_ACTUAL.getId());
                    }

                }
                else {
                    //Toast.makeText(this, "Sin comunidades asociadas", Toast.LENGTH_SHORT).show();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Sin comunidades asociadas", Snackbar.LENGTH_SHORT).show();
                }

                /*adapter = new ListViewAdapterComunidadInforme(array_informes_comunidades, this);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        BoletinModelo dataModel = array_informes_comunidades.get(position);
                    }
                });*/

                swipeLayout.setRefreshing(false);
                progressDialog.dismiss();
            }
        } catch (JSONException e) {
            // e.printStackTrace();
            swipeLayout.setRefreshing(false);
            progressDialog.dismiss();
            executeQuery_getReuniones(ClaseSingleton.SELECT_ALL_COUNT_REUNIONES_BY_ID + "?IdPersona=" + ClaseSingleton.USUARIO_ACTUAL.getId());
        }
        swipeLayout.setRefreshing(false);
        progressDialog.dismiss();
    }

    private void executeQuery_getBoletines(String URL) {

        //progressDialog.setMessage("Cargando boletines..."); progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        //errorMessageDialog(URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                obtenerDatosBoletinesResponse(response);  /* Para inicio de sesión*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                swipeLayout.setRefreshing(false);
                progressDialog.dismiss();
                errorMessageDialog("No se puede conectar al servidor en estos momentos.\nIntente conectarse más tarde.");
                // errorMessageDialog(error.toString());
            }
        });queue.add(stringRequest);
    }

    private void obtenerDatosReunionesResponse(String response){

        try{
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("false")){
                errorMessageDialog("No ha sido posible cargar las reuniones.\nVerifique su conexión a internet!");
            }
            else
            {
                if (!jsonObject.getString("status").equals("no hay comunidades"))
                {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String titular = jsonArray.getJSONObject(i).get("Titular").toString();
                        String provincia = jsonArray.getJSONObject(i).get("Provincia").toString();
                        String canton = jsonArray.getJSONObject(i).get("Canton").toString();
                        String fecha = jsonArray.getJSONObject(i).get("Fecha").toString();
                        String hora = jsonArray.getJSONObject(i).get("Hora").toString();
                        String descripcion = jsonArray.getJSONObject(i).get("Detalle").toString();
                        //String sospechosos = jsonArray.getJSONObject(i).get("Sospechosos").toString();
                        //String armasSosp = jsonArray.getJSONObject(i).get("ArmasSosp").toString();
                        //String vehiculosSosp = jsonArray.getJSONObject(i).get("VehiculosSosp").toString();
                        String linkImagenGPS = jsonArray.getJSONObject(i).get("EnlaceGPS").toString();
                        String nombreComu = jsonArray.getJSONObject(i).get("Comunidad").toString();

                        // Info usuario
                        String idAutor = jsonArray.getJSONObject(i).get("IdPersona").toString();
                        String nombreAutor = jsonArray.getJSONObject(i).get("Nombre").toString();
                        String apellidoAutor = jsonArray.getJSONObject(i).get("Apellido").toString();
                        String correoAutor = jsonArray.getJSONObject(i).get("Correo").toString();
                        String sobrenombreAutor = jsonArray.getJSONObject(i).get("sobrenombre").toString();
                        String lugarResidencia = jsonArray.getJSONObject(i).get("lugarResidencia").toString();
                        String generoAutor = jsonArray.getJSONObject(i).get("genero").toString();
                        String fechaNacimiento = jsonArray.getJSONObject(i).get("fechaNacimiento").toString();
                        String biografia = jsonArray.getJSONObject(i).get("biografia").toString();

                        Persona autor = new Persona();
                        autor.setId(Integer.valueOf(idAutor));
                        autor.setCorreo(correoAutor);
                        autor.setNombre(nombreAutor);
                        autor.setApellido(apellidoAutor);
                        autor.setFechaNacimiento(fechaNacimiento);
                        autor.setBiografia(biografia);
                        autor.setGenero(generoAutor);
                        autor.setLugarResidencia(lugarResidencia);
                        autor.setSobrenombre(sobrenombreAutor);

                        //System.out.println("Autor " + autor.getNombre());
                        //System.out.println("Autor " + autor.getId());
                        array_informes_comunidades.add(i,
                                new BoletinModelo(autor.getNombre() + autor.getApellido(), titular, nombreComu, canton, fecha, hora, descripcion,
                                        "", "", "", linkImagenGPS, autor, nombreComu, TipoInforme.REUNION.getNombreInforme()));
                    }

                }
                else {
                    //Toast.makeText(this, "Sin comunidades asociadas", Toast.LENGTH_SHORT).show();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Sin comunidades asociadas", Snackbar.LENGTH_SHORT).show();
                }

                Log.d("NEPE", String.valueOf(array_informes_comunidades.size()));

            }
        } catch (JSONException e) {
            //e.printStackTrace();
            Log.d("NEPE", "Comunidad asociada pero sin boletines y/o reuniones");
        }

        adapter = new ListViewAdapterComunidadInforme(array_informes_comunidades, this);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BoletinModelo dataModel = array_informes_comunidades.get(position);
            }
        });

        swipeLayout.setRefreshing(false);
        progressDialog.dismiss();
    }

    private void executeQuery_getReuniones(String URL) {

        progressDialog.setMessage("Cargando reuniones..."); progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        //errorMessageDialog(URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                obtenerDatosReunionesResponse(response);  /* Para inicio de sesión*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                swipeLayout.setRefreshing(false);
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
