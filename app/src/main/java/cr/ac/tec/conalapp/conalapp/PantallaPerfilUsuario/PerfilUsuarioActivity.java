package cr.ac.tec.conalapp.conalapp.PantallaPerfilUsuario;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.PantallaAgregarInfoPerfil.AgregarInfoPerfil;
import cr.ac.tec.conalapp.conalapp.R;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private TextView nombreUsuario;
    private TextView lugarResidencia;
    private TextView sobrenombre;
    private TextView anioNacimiento;
    private TextView genero;
    private TextView biografia;
    private TextView email;

    private ImageView imagenPerfil;
    private ImageView btn_editar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        inicializarComponentes();
    }

    private void inicializarComponentes(){
        nombreUsuario = findViewById(R.id.nombreUsuario);
        lugarResidencia = findViewById(R.id.lugarResidencia);
        sobrenombre = findViewById(R.id.sobrenombre);
        anioNacimiento = findViewById(R.id.anioNacimiento);
        genero = findViewById(R.id.genero);
        biografia = findViewById(R.id.biografia);
        email = findViewById(R.id.email);
        imagenPerfil = findViewById(R.id.imagenPerfil);
        btn_editar = findViewById(R.id.btn_editar);
        btn_editar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                editarPerfil();
            }

        });


        String tipoPerfil = getIntent().getStringExtra("TipoPerfil");
        if (tipoPerfil.equals("Ajeno")){
            nombreUsuario.setText(getIntent().getStringExtra("Nombre"));
            sobrenombre.setText(getIntent().getStringExtra("Sobrenombre"));
            lugarResidencia.setText(getIntent().getStringExtra("LugarResidencia"));
            genero.setText(getIntent().getStringExtra("Genero"));
            biografia.setText(getIntent().getStringExtra("Biografia"));
            anioNacimiento.setText(getIntent().getStringExtra("FechaNacimiento"));
            email.setText(getIntent().getStringExtra("Correo"));
        }else {
            nombreUsuario.setText(ClaseSingleton.USUARIO_ACTUAL.getNombre() + " " + ClaseSingleton.USUARIO_ACTUAL.getApellido());
            sobrenombre.setText(ClaseSingleton.USUARIO_ACTUAL.getSobrenombre());
            lugarResidencia.setText(ClaseSingleton.USUARIO_ACTUAL.getLugarResidencia());
            genero.setText(ClaseSingleton.USUARIO_ACTUAL.getGenero());
            biografia.setText(ClaseSingleton.USUARIO_ACTUAL.getBiografia());
            anioNacimiento.setText(ClaseSingleton.USUARIO_ACTUAL.getFechaNacimiento());
            email.setText(ClaseSingleton.USUARIO_ACTUAL.getCorreo());
        }
    }

    private void editarPerfil(){
        Intent intent = new Intent(this, AgregarInfoPerfil.class);
        startActivity(intent);

    }


}
