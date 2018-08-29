package cr.ac.tec.conalapp.conalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IniciarSesionActivity extends AppCompatActivity {

    private Button btn_ingresar;
    private TextView tv_crear_cuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        inicializarComponentes();
    }

    private void inicializarComponentes()
    {
        btn_ingresar = findViewById(R.id.btn_ingresar_id);
        btn_ingresar.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               iniciarSesion();
           }

        });

        tv_crear_cuenta = findViewById(R.id.crear_cuenta_id);
        tv_crear_cuenta.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                goto_resgistrarUsuario();
            }

        });
    }

    private void iniciarSesion()
    {
        Intent intent = new Intent(this, PrincipalActivity.class);
        startActivity(intent);
    }

    private void goto_resgistrarUsuario()
    {
        Intent intent = new Intent(this, RegistroUsuarioActivity.class);
        startActivity(intent);
    }
}
