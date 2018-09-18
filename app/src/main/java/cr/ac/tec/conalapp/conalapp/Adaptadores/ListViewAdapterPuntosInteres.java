package cr.ac.tec.conalapp.conalapp.Adaptadores;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.Modelo.PuntosInteresModelo;
import cr.ac.tec.conalapp.conalapp.R;

public class ListViewAdapterPuntosInteres extends ArrayAdapter<PuntosInteresModelo> implements View.OnClickListener {

    private ArrayList<PuntosInteresModelo> dataSet;
    private Context mContext;
    private ArrayList<PuntosInteresModelo> arrayListPuntosInteresModelo;
    //private int lastPosition = -1;
    private ProgressDialog progressDialog;
    // View lookup cache
    private static class RetenedorVista {
        TextView tv_provincia;
        TextView tv_canton;
        ImageButton ibtn_borrar_punto;
    }

    public ListViewAdapterPuntosInteres(ArrayList<PuntosInteresModelo> data, Context context) {
        super(context, R.layout.row_item_punto_interes, data);
        this.dataSet = data;
        this.mContext = context;
        this.arrayListPuntosInteresModelo = data;
    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        PuntosInteresModelo puntosInteres = (PuntosInteresModelo) object;

        switch (v.getId()) {
            case R.id.ibtn_borrar_punto_id:
                assert puntosInteres != null;

                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Eliminando punto de interés ...");
                progressDialog.show();
                executeQueryEliminarPuntoInteres(ClaseSingleton.DELETE_PUNTO_INTERES_BY_ID,
                         ((PuntosInteresModelo) object).getIdUsuario(),
                        ((PuntosInteresModelo) object).getProvincia(),
                        ((PuntosInteresModelo) object).getCanton(), object);
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        PuntosInteresModelo puntosInteresModelo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ListViewAdapterPuntosInteres.RetenedorVista retenedorVista; // view lookup cache stored in tag

        final View result;

        if (convertView == null)
        {
            retenedorVista = new ListViewAdapterPuntosInteres.RetenedorVista();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_punto_interes, parent, false);

            retenedorVista.tv_provincia = (TextView) convertView.findViewById(R.id.tv_provincia_id);
            retenedorVista.tv_canton = (TextView) convertView.findViewById(R.id.tv_canton_id);

            retenedorVista.ibtn_borrar_punto = (ImageButton) convertView.findViewById(R.id.ibtn_borrar_punto_id);

            result = convertView;

            convertView.setTag(retenedorVista);
        }
        else
        {
            retenedorVista = (ListViewAdapterPuntosInteres.RetenedorVista) convertView.getTag();
            result = convertView;
        }

        /*Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;*/

        retenedorVista.tv_provincia.setText(puntosInteresModelo.getProvincia());
        retenedorVista.tv_canton.setText(puntosInteresModelo.getCanton());
        retenedorVista.ibtn_borrar_punto.setTag(position);
        retenedorVista.ibtn_borrar_punto.setOnClickListener(this);

        // Return the completed view to render on screen
        return convertView;
    }

    public void refreshEvents() {
        //this.arrayListPuntosInteresModelo.clear();
        //this.arrayListPuntosInteresModelo.addAll(events);
        notifyDataSetChanged();
    }

    private void executeQueryEliminarPuntoInteres(String URL, final String IdUsuario, final String Provincia, final String Canton, final Object object) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // Si serv contesta
                System.out.println(response);
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("false")){
                        progressDialog.dismiss();
                        errorMessageDialog("No ha sido el punto de intere.\nVerifique su conexión a internet!");
                    }
                    else
                    {
                        arrayListPuntosInteresModelo.remove(object);
                        refreshEvents();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {  //Tratar errores conexion con serv
            @Override
            public void onErrorResponse(VolleyError error) {
                //MessageDialog(error.toString());
                errorMessageDialog("No se puede conectar al servidor en estos momentos.\nIntente conectarse más tarde.");
            }
        }) {
            @Override
            protected Map<String, String> getParams() { // Armar Map para enviar al serv mediante un POST
                System.out.print("get params");
                Map<String, String> params = new HashMap<String, String>();
                params.put("IdPersona", IdUsuario);
                params.put("Provincia", Provincia);
                params.put("Canton", Canton);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
/*
        RequestQueue queue = Volley.newRequestQueue(getContext());
        //errorMessageDialog(URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response)
            {
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("false")){
                        progressDialog.dismiss();
                        errorMessageDialog("No ha sido el punto de intere.\nVerifique su conexión a internet!");
                    }
                    else
                    {
                        arrayListPuntosInteresModelo.remove(object);
                        refreshEvents();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                errorMessageDialog("No se puede conectar al servidor en estos momentos.\nIntente conectarse más tarde.");
                // errorMessageDialog(error.toString());
            }
        });queue.add(stringRequest);*/
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