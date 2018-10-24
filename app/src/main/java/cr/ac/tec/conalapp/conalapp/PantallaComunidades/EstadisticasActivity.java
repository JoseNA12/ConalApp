package cr.ac.tec.conalapp.conalapp.PantallaComunidades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.R;

// https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/BarChartActivityMultiDataset.java

public class EstadisticasActivity extends AppCompatActivity {

    private Map<String, Integer> datos;
    private BarChart barChart;
    private float barWidth = 0.5f; // x4 DataSet
    private float barSpace = 0.015f; // x4 DataSet
    private float groupSpace = 0.03f;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        initComponentes();

        executeQuery(ClaseSingleton.SELECT_ALL_COMUNIDAD_WITH_COUNT_BOLETINES);
    }

    private void initComponentes()
    {
        initChar();
        initProgressDialog();
    }

    private void initChar()
    {
        barChart = findViewById(R.id.barChart_comunidades);
        barChart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);

        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        // hide the X legends
        barChart.getAxisRight().setEnabled(false);

    }

    private void initProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
    }

    private void ObtenerValoresComunidades()
    {
        // TODO: suponiendo que hago el request al servidor y me da un hashmap con las comunidades y
        // los valores de las cantidades de los actos delictivos
        /*datos = new HashMap<String, Integer>();
        datos.put("Comunidad 1", 12);
        datos.put("Comunidad 2", 23);
        datos.put("Comunidad 3", 6);
        datos.put("Comunidad 4", 17);
        datos.put("Comunidad 5", 5);
        datos.put("Comunidad 6", 7);
        datos.put("Comunidad 7", 17);
        datos.put("Comunidad 8", 18);
        datos.put("Comunidad 9", 9);*/

        //------------------------------------------------------------------------------------

        List<IBarDataSet> miSet = new ArrayList<>();
        int i = 0;

        for (Map.Entry<String, Integer> pDatos : datos.entrySet())
        {
            ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
            yVals.add(new BarEntry(i, (float) pDatos.getValue()));

            BarDataSet set = new BarDataSet(yVals, pDatos.getKey());

            set.setColor(DameUnColor());

            miSet.add(set);
            i++;
        }

        BarData data = new BarData(miSet);
        data.setValueFormatter(new LargeValueFormatter());

        barChart.setData(data);

        // specify the width each bar should have
        //barChart.getBarData().setBarWidth(barWidth);
        //barChart.getXAxis().setAxisMinimum(0);
        //barChart.getXAxis().setAxisMaximum(datos.size() + 1);
        //barChart.groupBars(0, groupSpace, barSpace);

        barChart.invalidate();
    }

    private int DameUnColor()
    {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    /*public void ejecutar()
    {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

        float randomMultiplier = 50 * 10f;
        float barWidth = 0.2f; // x4 DataSet

        yVals1.add(new BarEntry(0, (float) (Math.random() * randomMultiplier)));
        yVals2.add(new BarEntry(1, (float) (Math.random() * randomMultiplier)));
        yVals3.add(new BarEntry(2, (float) (Math.random() * randomMultiplier)));
        yVals4.add(new BarEntry(3, (float) (Math.random() * randomMultiplier)));

        BarDataSet set1, set2, set3, set4;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) barChart.getData().getDataSetByIndex(2);
            set4 = (BarDataSet) barChart.getData().getDataSetByIndex(3);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            set4.setValues(yVals4);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(yVals1, "Company A");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(yVals2, "Company B");
            set2.setColor(Color.rgb(164, 228, 251));
            set3 = new BarDataSet(yVals3, "Company C");
            set3.setColor(Color.rgb(242, 247, 158));
            set4 = new BarDataSet(yVals4, "Company D");
            set4.setColor(Color.rgb(255, 102, 0));

            BarData data = new BarData(set1, set2, set3, set4);
            data.setValueFormatter(new LargeValueFormatter());

            barChart.setData(data);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle preses on the actiob bar items
        switch (item.getItemId())
        {
            case R.id.action_refrescar_estadisticas:
                executeQuery(ClaseSingleton.SELECT_ALL_COMUNIDAD_WITH_COUNT_BOLETINES);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_action_bar_estadisticas_main, menu);
        return true;
    }

    private void obtenerDatosComunidadesWithCountBoletinesResponse(String response){
        datos = new HashMap<String, Integer>();

        try{
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("false")){
                MessageDialog("No ha sido posible cargar los datos.\nVerifique su conexión a internet!");
            }
            else
            {
                JSONArray jsonArray = new JSONObject(response).getJSONArray("value");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    String nombreComunidad = jsonArray.getJSONObject(i).get("Nombre").toString();
                    String cantidadBoletines = jsonArray.getJSONObject(i).get("CantidadBoletines").toString(); // TODO: Ojo al valor de la columna

                    datos.put(nombreComunidad, Integer.valueOf(cantidadBoletines));
                }

                ObtenerValoresComunidades();

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
                obtenerDatosComunidadesWithCountBoletinesResponse(response);  /* Para inicio de sesión*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                progressDialog.dismiss();
                MessageDialog("No se puede conectar al servidor en estos momentos.\nIntente conectarse más tarde.");
                // errorMessageDialog(error.toString());
            }
        });queue.add(stringRequest);
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

