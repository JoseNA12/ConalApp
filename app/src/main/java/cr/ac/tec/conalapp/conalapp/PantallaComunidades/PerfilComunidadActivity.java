package cr.ac.tec.conalapp.conalapp.PantallaComunidades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.R;

public class PerfilComunidadActivity extends AppCompatActivity {

    private String IdComu, Nombre, Provincia, Canton, Descripcion;
    private TextView tv_nombre_comunidad, tv_provincia, tv_canton, tv_descripcion;
    private ImageView btn_seguir_comunidad;

    private ProgressDialog progressDialog;

    private boolean sigueComunidad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_comunidad);

        initComponentes();
    }

    private void initComponentes()
    {
        tv_nombre_comunidad = (TextView) findViewById(R.id.tv_nombre_comunidad_id);
        tv_provincia = (TextView) findViewById(R.id.tv_provincia_id);
        tv_canton = (TextView) findViewById(R.id.tv_canton_id);
        tv_descripcion = (TextView) findViewById(R.id.tv_descripcion_id);
        btn_seguir_comunidad = (ImageView) findViewById(R.id.btn_seguir_comunidad_id);

        btn_seguir_comunidad.setOnClickListener(new View.OnClickListener(){
        public void onClick(View v){
            ProcesarSeguimiento();
        }
    });

        IdComu = getIntent().getStringExtra("IdComu");
        Nombre = getIntent().getStringExtra("Nombre");
        Provincia = getIntent().getStringExtra("Provincia");
        Canton = getIntent().getStringExtra("Canton");
        Descripcion = getIntent().getStringExtra("Descripcion");

        tv_nombre_comunidad.setText(Nombre);
        tv_provincia.setText(Provincia);
        tv_canton.setText(Canton);
        tv_descripcion.setText(Descripcion);

        initProgressDialog();

        //btn_seguir_comunidad.setImageResource(R.drawable.ic_seguir);

        /*executeQuery_CheckFollow(ClaseSingleton.CHECK_FOLLOW_COMUNIDAD_BY_ID_USUARIO,
                String.valueOf(ClaseSingleton.USUARIO_ACTUAL.getId()), IdComu);*/

        executeQuery_CheckFollow(ClaseSingleton.CHECK_FOLLOW_COMUNIDAD_BY_ID_USUARIO +
                "?IdPersona="+ ClaseSingleton.USUARIO_ACTUAL.getId() + "&IdComunidad=" + IdComu);
    }







    private void executeQuery_CheckFollow(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        //errorMessageDialog(URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                CheckFollowComunidad(response);  /* Para inicio de sesión*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("No se puede conectar al servidor en estos momentos.\nIntente conectarse más tarde.");
                // errorMessageDialog(error.toString());
            }
        });queue.add(stringRequest);
    }






    private void initProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
    }

    private void ProcesarSeguimiento()
    {
        if (sigueComunidad)
        {
            executeQuery_Dar_UnFollow(ClaseSingleton.UNFOLLOW_COMUNIDAD,
                    String.valueOf(ClaseSingleton.USUARIO_ACTUAL.getId()), IdComu);
        }
        else
        {
            executeQuery_Dar_Follow(ClaseSingleton.FOLLOW_COMUNIDAD,
                    String.valueOf(ClaseSingleton.USUARIO_ACTUAL.getId()), IdComu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle preses on the actiob bar items
        switch (item.getItemId())
        {
            case R.id.action_estadisticas_perfil:
                Intent i =  new Intent(getApplicationContext(), EstadisticasComunidadActivity.class);

                i.putExtra("IdComu", IdComu);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_action_bar_perfil_comunidad, menu);
        return true;
    }

    /*private void executeQuery_CheckFollow(String URL, final String IdPersona, final String IdComunidad) {

        progressDialog = ProgressDialog.show(this,"Atención","Cargando datos...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // Si serv contesta

                CheckFollowComunidad(response);
            }
        }, new Response.ErrorListener() {  //Tratar errores conexion con serv
            @Override
            public void onErrorResponse(VolleyError error) {
                //MessageDialog(error.toString());
                progressDialog.dismiss();
                MessageDialog("No se pudo cargar todos los datos del perfil.\nIntente conectarse más tarde.");
            }
        }) {
            @Override
            protected Map<String, String> getParams() { // Armar Map para enviar al serv mediante un POST
                System.out.print("get params");
                Map<String, String> params = new HashMap<String, String>();
                params.put("IdPersona", IdPersona);
                params.put("IdComunidad", IdComunidad);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }*/

    private void CheckFollowComunidad(String response){
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(response);
            Log.d("NEPE", response);

            if (jsonObject.getString("status").equals("false")){
                MessageDialog("No se puede conectar con el servidor. Inténtelo de nuevo más tarde.");
                //finish();
            }
            else {
                String respuesta = jsonObject.getString("value");


                Log.d("NEPE", jsonObject.getString("value"));

                if (respuesta.equals("unfollow"))
                {
                    btn_seguir_comunidad.setImageResource(R.drawable.ic_dejar_de_seguir);
                    sigueComunidad = false;
                }
                else
                {
                    if (jsonObject.getString("value").equals("follow"))
                    {
                        // MessageDialog("Se ha publicado la reunión correctamente.");
                        btn_seguir_comunidad.setImageResource(R.drawable.ic_seguir);
                        sigueComunidad = true;
                    }
                    else
                    {
                        // error
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void executeQuery_Dar_Follow(String URL, final String IdPersona, final String IdComunidad) {

        progressDialog = ProgressDialog.show(this,"Atención","Asociando comunidad...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // Si serv contesta
                //System.out.println(response);
                DarFollow_Response(response);
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
                System.out.print("get params");
                Map<String, String> params = new HashMap<String, String>();
                params.put("IdPersona", IdPersona);
                params.put("IdComunidad", IdComunidad);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void DarFollow_Response(String response){
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("false")){
                MessageDialog("No se pudo asociar la comunidad. Inténtelo de nuevo.");
            }
            else {
                //Toast.makeText(this, "Se ha asociado la comunidad a su cuenta", Toast.LENGTH_SHORT);

                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Se ha asociado la comunidad a su cuenta", Snackbar.LENGTH_SHORT).show();

                btn_seguir_comunidad.setImageResource(R.drawable.ic_seguir);
                sigueComunidad = true;

                // finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void executeQuery_Dar_UnFollow(String URL, final String IdPersona, final String IdComunidad) {

        progressDialog = ProgressDialog.show(this,"Atención","Quitando comunidad...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // Si serv contesta
                //System.out.println(response);
                DarUnFollow_Response(response);
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
                System.out.print("get params");
                Map<String, String> params = new HashMap<String, String>();
                params.put("IdPersona", IdPersona);
                params.put("IdComunidad", IdComunidad);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void DarUnFollow_Response(String response){
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("false")){
                MessageDialog("No se eliminar la comunidad de su cuenta. Inténtelo de nuevo.");
            }
            else {
                //Toast.makeText(this, "Se ha eliminado la comunidad de su cuenta", Toast.LENGTH_SHORT);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Se ha eliminado la comunidad de su cuenta", Snackbar.LENGTH_SHORT).show();

                btn_seguir_comunidad.setImageResource(R.drawable.ic_dejar_de_seguir);
                sigueComunidad = false;

                // finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void MessageDialog(String message) {
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
