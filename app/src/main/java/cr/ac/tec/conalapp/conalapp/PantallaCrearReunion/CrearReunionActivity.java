package cr.ac.tec.conalapp.conalapp.PantallaCrearReunion;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.PantallaCrearBoletin.CrearBoletinActivity;
import cr.ac.tec.conalapp.conalapp.R;

public class CrearReunionActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextInputEditText input_titular, input_proposito;
    private EditText input_fecha, input_hora;
    private Button btn_crear_reunion;

    private AutoCompleteTextView actv_comunidades;

    private Map<String, String> comunidadesActuales = new HashMap<String, String>(); // orden: <key, value>

    private String[] provincias, cantones_san_jose, cantones_alajuela, cantones_cartago,
            cantones_guanacaste, cantones_heredia, cantones_puntarenas, cantones_limon;

    private Switch sch_desea_ubicar_lugar;

    private DatePickerDialog datePickerDialog;

    private Spinner sp_provincias, sp_cantones_por_provincia;
    ProgressDialog progressDialog;

    private LinearLayout linear_layout;
    private ScrollView scrollView;

    private View miView;
    private ImageView ivMapTransparent;

    private static final String TAG = "NEPE";
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private View.OnTouchListener mListener;

    private Bitmap bitmap;
    private StorageReference imageReference;

    private StorageReference mStorageRef;

    private Uri downloadUri = null;

    private Marker posicionCentro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reunion);
        miView = findViewById(android.R.id.content);
        inicializarComponentes();

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        //setContentView(R.layout.activity_crear_boletin);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        // Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    private void inicializarComponentes()
    {
        input_titular = (TextInputEditText) findViewById(R.id.input_titular_id);
        input_proposito = (TextInputEditText) findViewById(R.id.input_proposito_id);
        btn_crear_reunion = (Button) findViewById(R.id.btn_crear_reunion_id);
        btn_crear_reunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_action_publicarReunion();
            }
        });

        actv_comunidades = (AutoCompleteTextView) findViewById(R.id.actv_comunidades_id);

        scrollView = (ScrollView) findViewById(R.id.scroll_view_id);

        initInputFecha();
        initInputHora();
        initSpinners();
        initFrameLayout();
        initImageView();
        initSwitchs();
        // initAutoCompleteTV();

        executeQuery_obtenerComunidades(ClaseSingleton.SELECT_ALL_COMUNIDAD);
    }

    private void initAutoCompleteTV()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, getNombreComunidades());
        //Getting the instance of AutoCompleteTextView
        actv_comunidades.setThreshold(1);//will start working from first character
        actv_comunidades.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv_comunidades.setTextColor(Color.rgb(204, 77, 41));
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
                datePickerDialog = new DatePickerDialog(CrearReunionActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CrearReunionActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        sch_desea_ubicar_lugar = (Switch) findViewById(R.id.sch_desea_ubicar_lugar_id);
        sch_desea_ubicar_lugar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    private void initImageView() // truco para mover verticalmente el mapa dento de un scrollview
    {
        ivMapTransparent = (ImageView) findViewById(R.id.ivMapTransparent);
        ivMapTransparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    private void establecerAdaptadorSPCantones(String[] pLista)
    {
        ArrayAdapter<String> adapter_cant = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, pLista);
        sp_cantones_por_provincia.setAdapter(adapter_cant);
    }

    private String[] getNombreComunidades() // hacer el request a la BD con los nombres de las comunidades
    {
        // TODO: El request debe trear el nombre de las comunidades junto a su ID (primary key)
        /*comunidadesActuales = new HashMap<String, String>();
        comunidadesActuales.put("1", "Comunidad 1");
        comunidadesActuales.put("2", "Comunidad 2");
        comunidadesActuales.put("3", "Comunidad 3");
        comunidadesActuales.put("4", "Comunidad 4");*/

        return diccionario_to_array(comunidadesActuales);
    }

    private String[] diccionario_to_array(Map<String, String> pDiccionario) // solo <String, String>
    {
        List<String> miLista = new ArrayList<String>();

        for (Map.Entry<String, String> pDatos : pDiccionario.entrySet())
        {
            miLista.add(pDatos.getValue());
        }
        return miLista.toArray(new String[comunidadesActuales.size()]);
    }

    private String getIDComunidad(String pNombreComunidadEscogida)
    {
        if (pNombreComunidadEscogida.trim().length() == 0) { return ""; } // no ingresó nada, almacene "" en la tabla
        for (Map.Entry<String, String> entry : comunidadesActuales.entrySet())
        {
            if (entry.getValue().equals(pNombreComunidadEscogida))
            {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                                @Override
                                public void onCameraIdle() {
                                    if (posicionCentro != null)
                                    {
                                        posicionCentro.remove();
                                    }
                                    posicionCentro = mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target));
                                }
                            });

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void captureScreen()
    {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback()
        {
            @Override
            public void onSnapshotReady(Bitmap snapshot)
            {
                ActivityCompat.requestPermissions(CrearReunionActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        12);

                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File myDir = new File(root + "/");
                myDir.mkdirs();

                String fname = "Image-" + System.currentTimeMillis() + ".jpg";
                File file = new File(myDir, fname);

                Bitmap bitmap = snapshot;

                try
                {
                    FileOutputStream out = new FileOutputStream(file);
                    // Write the string to the file
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);

                    out.flush();
                    out.close();

                    uploadImage(bitmap, fname);
                    //misDatos = new ArrayList<DatosImagen>();
                    //misDatos.add(new DatosImagen(bitmap, fname));

                    Log.d("ImageCapture", "terminó");
                }
                catch (FileNotFoundException e)
                {
                    // Log.d("ImageCapture", "FileNotFoundException"); Log.d("ImageCapture", e.getMessage());
                    // caso extremo
                    MessageDialog("Ha ocurrido algo innesperado al tratar de almacenar la imagen GPS!");

                }
                catch (IOException e)
                {
                    MessageDialog("Es necesario poseer permisos de almacenamiento para utlizar el GPS!");
                    // Log.d("ImageCapture", "IOException"); Log.d("ImageCapture", e.getMessage());
                }
            }
        };

        mMap.snapshot(callback);
    }

    public void uploadImage(Bitmap bitmap, String fname) {

        progressDialog = ProgressDialog.show(CrearReunionActivity.this,"Atención","Adjuntando imagen de la señal GPS...");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // StorageReference storageRef = storage.getReferenceFromUrl("gs://conalapp-74fc6.appspot.com");
        final StorageReference imagesRef = mStorageRef.child("images/" + fname);

        final UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return imagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadUri = task.getResult();
                            progressDialog.dismiss();
                            procesarSolicitudReunion(downloadUri.toString());
                        }
                        else
                        {
                            progressDialog.dismiss();
                            MessageDialog("Ha ocurrido un error al obtener la imagen GPS.\n" +
                                    "Intente nuevamente maás tarde!");
                        }
                    }
                });
            }
        });
    }

    private void btn_action_publicarReunion()
    {
        if (getIDComunidad(actv_comunidades.getText().toString()) != null)
        {
            if (!input_titular.getText().toString().trim().equals(""))
            {
                if (!input_hora.getText().toString().equals("") && !input_fecha.getText().toString().equals(""))
                {
                    if (sch_desea_ubicar_lugar.isChecked())
                    {
                        // https://www.journaldev.com/9708/android-asynctask-example-tutorial
                        captureScreen(); // -> despues de tomar la captura y subir la imagen, llama a registrarBoletinBD
                    }
                    else
                    {
                        procesarSolicitudReunion(ClaseSingleton.linkImagenGPSNoDisponible);
                    }
                }
                else
                {
                    MessageDialog("Debe ingresar una fecha y hora del acontecimiento!");
                }
            }
            else
            {
                MessageDialog("Debe ingresar un titular!");
            }
        }
        else
        {
            MessageDialog("La comunidad ingresada no existe!. Por favor, verifique el nombre ingresado.");
        }
    }

    private void procesarSolicitudReunion(String pEnlaceImagen)
    {
        executeQuery(ClaseSingleton.INSERT_REUNION, String.valueOf(ClaseSingleton.USUARIO_ACTUAL.getId()),
                sp_provincias.getSelectedItem().toString(), sp_cantones_por_provincia.getSelectedItem().toString(),
                input_titular.getText().toString(), input_proposito.getText().toString(), input_hora.getText().toString(),
                input_fecha.getText().toString(), pEnlaceImagen, getIDComunidad(actv_comunidades.getText().toString()));
    }

    private void executeQuery(String URL, final String IdPersona, final String Provincia, final String Canton,
                              final String Titular, final String Descripcion, final String Hora,
                              final String Fecha, final String EnlaceGPS, final String IdComunidad) {

        progressDialog = ProgressDialog.show(this,"Atención","Publicando reunión...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // Si serv contesta
                System.out.println(response);
                RegistrarReunion_Response(response);
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
                params.put("Titular", Titular);
                params.put("Provincia", Provincia);
                params.put("Canton", Canton);
                params.put("Fecha", Fecha);
                params.put("Hora", Hora);
                params.put("Descripcion", Descripcion);
                params.put("EnlaceGPS", EnlaceGPS);
                params.put("IdComunidad", IdComunidad);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void RegistrarReunion_Response(String response){
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("false")){
                MessageDialog("No se pudo publicar la reunión. Inténtelo de nuevo.");
            }
            else {
                // MessageDialog("Se ha publicado la reunión correctamente.");
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void obtenerDatosComunidadesResponse(String response){
        comunidadesActuales = new HashMap<String, String>();

        try{
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("false")){
                MessageDialog("No ha sido posible cargar los datos de las comunidades.\nVerifique su conexión a internet!");
            }
            else
            {
                JSONArray jsonArray = new JSONObject(response).getJSONArray("value");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    String idComunidad = jsonArray.getJSONObject(i).get("IdComunidad").toString();
                    String nombreComunidad = jsonArray.getJSONObject(i).get("Comunidad").toString();

                    comunidadesActuales.put(idComunidad, nombreComunidad);
                }
                initAutoCompleteTV();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void executeQuery_obtenerComunidades(String URL) {

        progressDialog = ProgressDialog.show(CrearReunionActivity.this,"Atención","Obteniendo datos...");

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
                MessageDialog("No ha sido posible obtener las comunidades. " +
                        "En caso de querer asociar la publicación a una comunidad debe intentarlo más tarde.");
                // errorMessageDialog(error.toString());
            }
        });queue.add(stringRequest);
    }

    private void MessageDialog(String message){ // mostrar mensaje emergente
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(message).setTitle("Atención!").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
