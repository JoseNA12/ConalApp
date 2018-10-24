package cr.ac.tec.conalapp.conalapp.PantallaComunidades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cr.ac.tec.conalapp.conalapp.R;

public class PerfilComunidadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_comunidad);

        // TODO: recibir el parametro que envia BusquedaComunidadesActivity (ID de la comunidad)
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle preses on the actiob bar items
        switch (item.getItemId())
        {
            case R.id.action_estadisticas_perfil:
                Intent i =  new Intent(getApplicationContext(), EstadisticasComunidadActivity.class);

                // TODO: enviar el parametro a EstadisticasComunidadActivity (ID de la comunidad)
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_action_bar_perfil_comunidad, menu);
        return true;
    }
}
