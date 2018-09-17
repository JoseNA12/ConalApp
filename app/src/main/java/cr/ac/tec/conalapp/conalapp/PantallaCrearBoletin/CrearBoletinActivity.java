package cr.ac.tec.conalapp.conalapp.PantallaCrearBoletin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.R;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class CrearBoletinActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextInputEditText input_titular, input_descripcion;
    private EditText input_fecha, input_hora;
    private Button btn_crear_boletin;

    private Switch sch_acordas_sospechosos, sch_armas_utilizadas, sch_vehiculos_usados,
                    sch_acordas_ubicacion;
    private TextInputEditText input_descripcion_sospechosos, input_descripcion_armas_usadas,
                                input_descripcion_vehiculos_usados;

    private String[] provincias, cantones_san_jose, cantones_alajuela, cantones_cartago,
            cantones_guanacaste, cantones_heredia, cantones_puntarenas, cantones_limon;

    private DatePickerDialog datePickerDialog;

    private Spinner sp_provincias, sp_cantones_por_provincia;

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

    ProgressDialog progressDialog;

    // El salva tandas
    // https://support.maps.me/hc/en-us/articles/207801769-The-app-cannot-determine-my-location


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_boletin);
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
        input_descripcion = (TextInputEditText) findViewById(R.id.input_descripcion_id);
        btn_crear_boletin = (Button) findViewById(R.id.btn_crear_boletin_id);
        btn_crear_boletin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_action_publicarBoletin();
            }
        });

        input_descripcion_sospechosos = (TextInputEditText) findViewById(R.id.input_descripcion_sospechosos_id);
        input_descripcion_armas_usadas = (TextInputEditText) findViewById(R.id.input_descripcion_armas_usadas_id);
        input_descripcion_vehiculos_usados = (TextInputEditText) findViewById(R.id.input_descripcion_vehiculos_usados_id);

        scrollView = (ScrollView) findViewById(R.id.scroll_view_id);

        initFrameLayout();
        initInputFecha();
        initInputHora();
        initImageView();
        initSpinners();
        initSwitchs();

        // FirebaseOptions opts = FirebaseApp.getInstance().getOptions();
        // Log.i(TAG, "Bucket = " + opts.getStorageBucket());
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
                ActivityCompat.requestPermissions(CrearBoletinActivity.this,
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

        progressDialog = ProgressDialog.show(CrearBoletinActivity.this,
                "Atención",
                "Adjuntando imagen de la señal GPS...");

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
                            procesarSolicitudBoletin(downloadUri.toString());
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

    private void btn_action_publicarBoletin()
    {
        if (!input_titular.getText().toString().trim().equals(""))
        {
            if (!input_hora.getText().toString().equals("") && !input_fecha.getText().toString().equals(""))
            {
                if (sch_acordas_ubicacion.isChecked())
                {
                    // https://www.journaldev.com/9708/android-asynctask-example-tutorial
                    captureScreen(); // -> despues de tomar la captura y subir la imagen, llama a registrarBoletinBD
                }
                else
                {
                    procesarSolicitudBoletin(ClaseSingleton.linkImagenGPSNoDisponible);
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

    private void procesarSolicitudBoletin(String pEnlaceImagen)
    {
        String stringSospechosos = "Sin información!";
        String stringArmas = "Sin información";
        String stringVehiculos = "Sin información!";

        // -------------OPCIONALES --------------
        // en caso de no ingresar nada o meter espacios vacios
        // los valores de "Sin información" permanecen almacenando eso (para no anidar tanto xD)
        if (sch_acordas_sospechosos.isChecked())
        {
            if (!input_descripcion_sospechosos.getText().toString().trim().equals(""))
            {
                stringSospechosos = input_descripcion_sospechosos.getText().toString();
            }

            if (sch_armas_utilizadas.isChecked())
            {
                if (!input_descripcion_armas_usadas.getText().toString().toString().trim().equals(""))
                {
                    stringArmas = input_descripcion_armas_usadas.getText().toString().toString();
                }
            }

            if (sch_vehiculos_usados.isChecked())
            {
                if (!input_descripcion_vehiculos_usados.getText().toString().toString().trim().equals(""))
                {
                    stringVehiculos = input_descripcion_vehiculos_usados.getText().toString().toString();
                }
            }
        }
        RegistrarBoletinBD(ClaseSingleton.INSERT_BOLETIN +
                "?IdPersona=" + 1 +
                "&Titular=" + input_titular.getText().toString() +
                "&Provincia=" + sp_provincias.getSelectedItem().toString() +
                "&Canton=" + sp_cantones_por_provincia.getSelectedItem().toString() +
                "&Fecha=" + input_fecha.getText().toString() +
                "&Hora=" + input_hora.getText().toString() +
                "&Descripcion=" + input_descripcion.toString() +
                "&Sospechosos=" + stringSospechosos +
                "&ArmasSosp=" + stringArmas +
                "&VehiculosSosp=" + stringVehiculos +
                "&EnlaceGPS=" + pEnlaceImagen
        );
    }

    private void RegistrarBoletinBD(String BD)
    {
        progressDialog = ProgressDialog.show(CrearBoletinActivity.this,
                "Atención",
                "Publicando boletín...");


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        // MessageDialog("Se ha creado el enlace!", "Éxito", "Aceptar");
                        Snackbar.make(CrearBoletinActivity.this.findViewById(android.R.id.content),
                                "Se ha creado el enlace!", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        MessageDialog("Error al publicar el boletín!");
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde!.");
            }
        });queue.add(stringRequest);
    }

    /**
     * Despliega un mensaje emergente en pantalla
     * @param message
     * @param pTitulo
     * @param pLabelBoton
     */
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