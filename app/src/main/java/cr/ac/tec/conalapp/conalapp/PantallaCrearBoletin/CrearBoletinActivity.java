package cr.ac.tec.conalapp.conalapp.PantallaCrearBoletin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.TextInputEditText;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.R;

public class CrearBoletinActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextInputEditText input_titular, input_descripcion;
    private EditText input_fecha, input_hora;
    private Button btn_crear_reunion;

    private Switch sch_acordas_sospechosos, sch_armas_utilizadas, sch_vehiculos_usados,
                    sch_acordas_ubicacion;
    private TextInputEditText input_descripcion_sospechosos, input_descripcion_armas_usadas,
                                input_descripcion_vehiculos_usados;

    private String[] provincias, cantones_san_jose, cantones_alajuela, cantones_cartago,
            cantones_guanacaste, cantones_heredia, cantones_puntarenas, cantones_limon;

    private DatePickerDialog datePickerDialog;

    private Spinner sp_provincias, sp_cantones_por_provincia;

    private LinearLayout linear_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crear_boletin);

        inicializarComponentes();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void inicializarComponentes()
    {
        input_titular = (TextInputEditText) findViewById(R.id.input_titular_id);
        input_descripcion = (TextInputEditText) findViewById(R.id.input_descripcion_id);
        btn_crear_reunion = (Button) findViewById(R.id.btn_crear_reunion_id);
        btn_crear_reunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                // startActivity(intent);
            }
        });

        input_descripcion_sospechosos = (TextInputEditText) findViewById(R.id.input_descripcion_sospechosos_id);
        input_descripcion_armas_usadas = (TextInputEditText) findViewById(R.id.input_descripcion_armas_usadas_id);
        input_descripcion_vehiculos_usados = (TextInputEditText) findViewById(R.id.input_descripcion_vehiculos_usados_id);

        initFrameLayout();
        initInputFecha();
        initInputHora();
        initSpinners();
        initSwitchs();
    }

    private void initInputFecha()
    {
        input_fecha = (EditText) findViewById(R.id.input_fecha_id);
        input_fecha.setInputType(InputType.TYPE_NULL); // no mostrar el teclado
        input_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int calendarYear = calendar.get(Calendar.YEAR);
                int calendarMonth = calendar.get(Calendar.MONTH);
                int calendarDay = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(CrearBoletinActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strMonth = String.valueOf(monthOfYear + 1);
                        String strDay = String.valueOf(dayOfMonth);
                        if ((monthOfYear + 1) < 10) strMonth = "0" + strMonth;
                        if (dayOfMonth < 10) strDay = "0" + strDay;
                        input_fecha.setText(year + "-" + strMonth + "-" + strDay);
                    }
                }, calendarYear, calendarMonth, calendarDay);
                datePickerDialog.show();
            }
        });
    }

    private void initInputHora()
    {
        input_hora = (EditText) findViewById(R.id.input_hora_id);
        input_hora.setInputType(InputType.TYPE_NULL); // no mostrar el teclado
        input_hora.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CrearBoletinActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        input_hora.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.show();

            }
        });
    }

    private void initSpinners()
    {
        provincias = getResources().getStringArray(R.array.array_provincias_costa_rica);
        cantones_san_jose = getResources().getStringArray(R.array.array_cantones_san_jose);
        cantones_alajuela = getResources().getStringArray(R.array.array_cantones_alajuela);
        cantones_cartago = getResources().getStringArray(R.array.array_cantones_cartago);
        cantones_guanacaste = getResources().getStringArray(R.array.array_cantones_guanacaste);
        cantones_heredia = getResources().getStringArray(R.array.array_cantones_heredia);
        cantones_puntarenas = getResources().getStringArray(R.array.arrar_cantones_puntarenas);
        cantones_limon = getResources().getStringArray(R.array.array_cantones_limon);

        sp_cantones_por_provincia = (Spinner)findViewById(R.id.sp_cantones_id);

        sp_provincias = (Spinner)findViewById(R.id.sp_provincias_id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, provincias);
        sp_provincias.setAdapter(adapter);

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

    private void initSwitchs()
    {
        sch_vehiculos_usados = (Switch) findViewById(R.id.sch_vehiculos_usados_id);
        sch_vehiculos_usados.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    input_descripcion_vehiculos_usados.setVisibility(View.VISIBLE);
                }
                else
                {
                    input_descripcion_vehiculos_usados.setVisibility(View.GONE);
                }
            }
        });

        sch_armas_utilizadas = (Switch) findViewById(R.id.sch_armas_utilizadas_id);
        sch_armas_utilizadas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    input_descripcion_armas_usadas.setVisibility(View.VISIBLE);
                }
                else
                {
                    input_descripcion_armas_usadas.setVisibility(View.GONE);
                }
            }
        });

        sch_acordas_sospechosos = (Switch) findViewById(R.id.sch_acordas_sospechosos_id);
        sch_acordas_sospechosos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    input_descripcion_sospechosos.setVisibility(View.VISIBLE);
                    sch_armas_utilizadas.setVisibility(View.VISIBLE);
                    sch_vehiculos_usados.setVisibility(View.VISIBLE);
                }
                else
                {
                    input_descripcion_sospechosos.setVisibility(View.GONE);
                    sch_armas_utilizadas.setChecked(false);
                    sch_armas_utilizadas.setVisibility(View.GONE);
                    input_descripcion_armas_usadas.setVisibility(View.GONE);
                    sch_vehiculos_usados.setChecked(false);
                    sch_vehiculos_usados.setVisibility(View.GONE);
                    input_descripcion_vehiculos_usados.setVisibility(View.GONE);
                }
            }
        });

        sch_acordas_ubicacion = (Switch) findViewById(R.id.sch_acordas_ubicacion_id);
        sch_acordas_ubicacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    linear_layout.setVisibility(View.VISIBLE);
                }
                else
                {
                    linear_layout.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initFrameLayout()
    {
        linear_layout = (LinearLayout) findViewById(R.id.linear_layout_id);
        linear_layout.setId(1230);

    }

    private void establecerAdaptadorSPCantones(String[] pLista)
    {
        ArrayAdapter<String> adapter_cant = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, pLista);
        sp_cantones_por_provincia.setAdapter(adapter_cant);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(37.4219999,-122.0862462))
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();

        //map.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex)); // sin animacion
        map.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);


        map.addMarker(new MarkerOptions()
                .position(new LatLng(37.4219999, -122.0862462))
                .title("Google Plex")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(37.4629101,-122.2449094))
                .title("Facebook")
                .snippet("Facebook HQ: Menlo Park"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(37.3092293,-122.1136845))
                .title("Apple"));

        map.setMyLocationEnabled(true); // Posicion actual
    }

}
