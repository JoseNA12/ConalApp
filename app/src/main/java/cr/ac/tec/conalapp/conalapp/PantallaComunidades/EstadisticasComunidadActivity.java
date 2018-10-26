package cr.ac.tec.conalapp.conalapp.PantallaComunidades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.R;

// https://github.com/PhilJay/MPAndroidChart

public class EstadisticasComunidadActivity extends AppCompatActivity {

    private int cantidadBoletines, cantidadReuniones = 0;
    private PieChart mChart;
    private ProgressDialog progressDialog;
    private String IDComunidadActual;

    private TextView tv_cant_boletines, tv_cant_reuniones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas_comunidad);

        initComponentes();
    }

    private void initComponentes()
    {
        initProgressDialog();
        initCharPastel();
        initTextViews();

        // TODO: recibir el parametro que envia PerfilComunidadActivity (ID de la comunidad)

        IDComunidadActual = getIntent().getStringExtra("IdComu");

        executeQuery_Boletines(ClaseSingleton.SELECT_ALL_COUNT_BOLETINES_BY_ID_COMUNIDAD + "?IdComunidad=" + IDComunidadActual);
    }

    private void initTextViews()
    {
        tv_cant_boletines = (TextView) findViewById(R.id.tv_cant_boletines_id);
        tv_cant_reuniones = (TextView) findViewById(R.id.tv_cant_reuniones_id);
    }

    private void initCharPastel()
    {
        mChart = findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 5, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        //mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);


        // setData(2, 100);

        mChart.animateY(1400);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTextSize(14f);
    }

    private void initProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando información...");
        progressDialog.setCancelable(false);
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //for (int i = 0; i < count ; i++) {
            //entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), "Labell"));
        //}

        int total = this.cantidadBoletines + this.cantidadReuniones;

        entries.add(new PieEntry((float) ((this.cantidadBoletines * mult) / total), "Boletines"));
        entries.add(new PieEntry((float) ((this.cantidadReuniones * mult) / total), "Reuniones"));

        PieDataSet dataSet = new PieDataSet(entries, "Datos");

        //dataSet.setDrawIcons(false);

        //dataSet.setSliceSpace(3f);
        //dataSet.setIconsOffset(new MPPointF(0, 40));
        //dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    /*private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 16, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 16, s.length() - 17, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 16, s.length() - 17, 0);

        s.setSpan(new RelativeSizeSpan(.8f), 16, s.length() - 17, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 17, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 17, s.length(), 0);
        return s;
    }*/

    private void obtenerDatosCantidadBoletinesResponse(String response){

        try{
            JSONObject jsonObject = new JSONObject(response);

            Log.d("NEPE", jsonObject.getString("status"));

            if (jsonObject.getString("status").equals("false")){
                MessageDialog("No ha sido posible cargar los datos de boletines.\nVerifique su conexión a internet!");
            }
            else
            {
                JSONArray jsonArray = new JSONObject(response).getJSONArray("value");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    // String cantidadBoletines = jsonObject.getJSONObject("value").getString("IdPersona");
                    String cantidadBoletines = jsonArray.getJSONObject(i).get("CantidadBoletines").toString();

                    this.cantidadBoletines = Integer.valueOf(cantidadBoletines);

                    tv_cant_boletines.setText("Cantidad de boletines: " + cantidadBoletines);
                }
                progressDialog.dismiss();
                executeQuery_Reuniones(ClaseSingleton.SELECT_ALL_COUNT_REUNIONES_BY_ID_COMUNIDAD + "?IdComunidad=" + IDComunidadActual);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void executeQuery_Boletines(String URL) {
        progressDialog = ProgressDialog.show(this,"Atención","Cargando boletines...");

        RequestQueue queue = Volley.newRequestQueue(this);
        //errorMessageDialog(URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                obtenerDatosCantidadBoletinesResponse(response);  /* Para inicio de sesión*/

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

    private void obtenerDatosCantidadReunionesResponse(String response){

        try{
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("false")){
                MessageDialog("No ha sido posible cargar los datos de las reuniones.\nVerifique su conexión a internet!");
            }
            else
            {
                JSONArray jsonArray = new JSONObject(response).getJSONArray("value");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    String cantidadReuniones = jsonArray.getJSONObject(i).get("CantidadReuniones").toString(); // TODO: Ojo al valor de la columna

                    this.cantidadReuniones = Integer.valueOf(cantidadReuniones);

                    tv_cant_reuniones.setText("Cantidad de reuniones: " + cantidadReuniones);
                }

                setData(2, 100);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void executeQuery_Reuniones(String URL) {
        progressDialog = ProgressDialog.show(this,"Atención","Cargando reuniones...");

        RequestQueue queue = Volley.newRequestQueue(this);
        //errorMessageDialog(URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                obtenerDatosCantidadReunionesResponse(response);  /* Para inicio de sesión*/

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
