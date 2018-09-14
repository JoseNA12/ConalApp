package cr.ac.tec.conalapp.conalapp.PantallaCrearBoletin;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cr.ac.tec.conalapp.conalapp.R;

public class CrearBoletinActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_boletin);
    }
}
