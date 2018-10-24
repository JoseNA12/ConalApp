package cr.ac.tec.conalapp.conalapp.PantallaComunidades;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cr.ac.tec.conalapp.conalapp.R;

// https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/BarChartActivityMultiDataset.java

public class EstadisticasActivity extends AppCompatActivity {

    private BarChart barChart;
    private float barWidth = 0.5f; // x4 DataSet
    private float barSpace = 0.015f; // x4 DataSet
    private float groupSpace = 0.03f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        initChar();

        ObtenerValoresComunidades();
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

    private void ObtenerValoresComunidades()
    {
        // TODO: suponiendo que hago el request al servidor y me da un hashmap con las comunidades y
        // los valores de las cantidades de los actos delictivos
        Map<String, Integer> datos = new HashMap<String, Integer>();
        datos.put("Comunidad 1", 12);
        datos.put("Comunidad 2", 23);
        datos.put("Comunidad 3", 6);
        datos.put("Comunidad 4", 17);
        datos.put("Comunidad 5", 5);
        datos.put("Comunidad 6", 7);
        datos.put("Comunidad 7", 17);
        datos.put("Comunidad 8", 18);
        datos.put("Comunidad 9", 9);

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

        Log.d("NEPE", barChart.getLegend().getEntries().toString());
    }

    private int DameUnColor()
    {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    public void ejecutar()
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle preses on the actiob bar items
        switch (item.getItemId())
        {
            case R.id.action_refrescar_estadisticas:
                ObtenerValoresComunidades();
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

    /* NO BORRAR
    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Provincias");

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
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Actos delictivos\ndatos del momento");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 16, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 16, s.length() - 17, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 16, s.length() - 17, 0);

        s.setSpan(new RelativeSizeSpan(.8f), 16, s.length() - 17, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 17, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 17, s.length(), 0);
        return s;
    }
    */
}

