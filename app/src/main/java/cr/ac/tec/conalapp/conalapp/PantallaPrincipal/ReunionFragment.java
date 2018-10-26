package cr.ac.tec.conalapp.conalapp.PantallaPrincipal;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import cr.ac.tec.conalapp.conalapp.Adaptadores.ListViewAdapterReunion;
import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.Modelo.Persona;
import cr.ac.tec.conalapp.conalapp.Modelo.ReunionModelo;
import cr.ac.tec.conalapp.conalapp.Modelo.TipoInforme;
import cr.ac.tec.conalapp.conalapp.PantallaCrearReunion.CrearReunionActivity;
import cr.ac.tec.conalapp.conalapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReunionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeLayout;
    private View view;

    private ArrayList<ReunionModelo> array_reuniones;
    private ListView listView;
    private static ListViewAdapterReunion adapter;

    private FloatingActionButton floatingActionButtonCrearReunion;
    private ProgressDialog progressDialog;

    public ReunionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reunion, container, false);
        inicializarComponentes(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void inicializarComponentes(View pView)
    {
        initProgressDialog();
        initSwipeLayout(pView);
        initListView(pView);
        initFloatingActiobButton(pView);
    }

    private void initProgressDialog()
    {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
    }

    private void initSwipeLayout(View pView)
    {
        swipeLayout = (SwipeRefreshLayout) pView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_blue_dark);
    }

    private void initListView(View pView)
    {
        listView = (ListView) view.findViewById(R.id.lv_reuniones_id);

        progressDialog.show();
        executeQuery(ClaseSingleton.SELECT_ALL_REUNION + "?IdPersona=" + ClaseSingleton.USUARIO_ACTUAL.getId());
    }

    private void initFloatingActiobButton(View pView)
    {
        floatingActionButtonCrearReunion = (FloatingActionButton) pView.findViewById(R.id.fab_crear_reunion);
        floatingActionButtonCrearReunion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CrearReunionActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * As a trick, you may be found interesting to disable manual swipe gesture,
     * maybe temporarily or because you only want to show progress animation programmatically.
     * What you need to do is to use the method setEnabled() and set it to false.
     */
    @Override
    public void onRefresh() {
        executeQuery(ClaseSingleton.SELECT_ALL_REUNION + "?IdPersona=" + ClaseSingleton.USUARIO_ACTUAL.getId());
    }


    private void obtenerDatosBoletinesResponse(String response){
        array_reuniones = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("false")){
                errorMessageDialog("No ha sido posible cargar las reuniones.\nVerifique su conexión a internet!");
            }
            else
            {
                JSONArray jsonArray = new JSONObject(response).getJSONArray("value");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    String titular = jsonArray.getJSONObject(i).get("Titular").toString();
                    String detalle = jsonArray.getJSONObject(i).get("Detalle").toString();
                    String fecha = jsonArray.getJSONObject(i).get("Fecha").toString();
                    String provincia = jsonArray.getJSONObject(i).get("Provincia").toString();
                    String hora = jsonArray.getJSONObject(i).get("Hora").toString();
                    String canton = jsonArray.getJSONObject(i).get("Canton").toString();
                    String linkImagenGPS = jsonArray.getJSONObject(i).get("EnlaceGPS").toString();
                    String IdComunidad = jsonArray.getJSONObject(i).get("IdComunidad").toString();

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

                    array_reuniones.add(i,
                            new ReunionModelo(autor.getNombre() + autor.getApellido(), titular,
                                    provincia, fecha, hora, linkImagenGPS, detalle, canton, autor, IdComunidad, TipoInforme.REUNION.getNombreInforme()));
                }

                adapter = new ListViewAdapterReunion(array_reuniones, getContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ReunionModelo dataModel = array_reuniones.get(position);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        swipeLayout.setRefreshing(false);
        progressDialog.dismiss();
    }

    private void executeQuery(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
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

    private void errorMessageDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
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
