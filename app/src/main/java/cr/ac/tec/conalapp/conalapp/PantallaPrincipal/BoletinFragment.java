package cr.ac.tec.conalapp.conalapp.PantallaPrincipal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

import cr.ac.tec.conalapp.conalapp.Adaptadores.ListViewAdapterBoletin;
import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.Modelo.BoletinModelo;
import cr.ac.tec.conalapp.conalapp.PantallaCrearBoletin.CrearBoletinActivity;
import cr.ac.tec.conalapp.conalapp.Modelo.Persona;
import cr.ac.tec.conalapp.conalapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BoletinFragment extends Fragment {

    private SwipeRefreshLayout swipeLayout;
    private View view;
    private ArrayList<BoletinModelo> array_boletines;
    private ListView listView;
    private static ListViewAdapterBoletin adapter;

    private FloatingActionButton floatingActionButtonCrearBoletin;
    public BoletinFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_boletin, container, false);
        inicializarComponentes(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void inicializarComponentes(View pView)
    {
        initSwipeLayout(pView);
        initListView(pView);
        initFloatingActiobButton(pView);
    }

    private void initSwipeLayout(View pView)
    {
        swipeLayout = (SwipeRefreshLayout) pView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        obtenerDatosBoletinesResponse(ClaseSingleton.SELECT_ALL_BOLETIN);
                        swipeLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        swipeLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_blue_dark);
    }

    private void initListView(View pView)
    {
        listView = (ListView) view.findViewById(R.id.lv_boletines_id);

        executeQuery(ClaseSingleton.SELECT_ALL_BOLETIN);
    }

    private void initFloatingActiobButton(View pView)
    {
        floatingActionButtonCrearBoletin = (FloatingActionButton) pView.findViewById(R.id.fab_crear_boletin);
        floatingActionButtonCrearBoletin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CrearBoletinActivity.class);
                startActivity(intent);
            }
        });
    }

    private void obtenerDatosBoletinesResponse(String response){
        array_boletines = new ArrayList<>();

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

                    System.out.println("Autor " + autor.getNombre());
                    System.out.println("Autor " + autor.getId());
                    array_boletines.add(0,
                            new BoletinModelo(autor.getNombre() + autor.getApellido(), titular, provincia, canton, fecha, hora, descripcion,
                                    sospechosos, armasSosp, vehiculosSosp, linkImagenGPS, autor));
                }

                adapter = new ListViewAdapterBoletin(array_boletines, getContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        BoletinModelo dataModel = array_boletines.get(position);
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
