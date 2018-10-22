package cr.ac.tec.conalapp.conalapp.PantallaComunidades;

import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.R;

public class CrearComunidadActivity extends AppCompatActivity {

    private ListView listView;
    private Button btn_comunidad;
    private Spinner sp_provincias, sp_cantones_por_provincia;

    private String[] provincias, cantones_san_jose, cantones_alajuela, cantones_cartago,
            cantones_guanacaste, cantones_heredia, cantones_puntarenas, cantones_limon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_comunidad);
        inicializarComponentes();

    }

    private void inicializarComponentes()
    {
        initListView();
        initBoton();
        initSpinner();
    }

    private void initListView()
    {
        listView = (ListView) findViewById(R.id.lv_boletines_id);

        // executeQuery(ClaseSingleton.SELECT_ALL_BOLETIN + "?IdPersona=" + ClaseSingleton.USUARIO_ACTUAL.getId());
    }

    private void initBoton()
    {
        btn_comunidad = (Button) findViewById(R.id.btn_crear_reunion_id);
    }

    private void initSpinner()
    {
        provincias = agregarOpcion(getResources().getStringArray(R.array.array_provincias_costa_rica), false);

        cantones_san_jose = agregarOpcion(getResources().getStringArray(R.array.array_cantones_san_jose), true);
        cantones_alajuela = agregarOpcion(getResources().getStringArray(R.array.array_cantones_alajuela), true);
        cantones_cartago = agregarOpcion(getResources().getStringArray(R.array.array_cantones_cartago), true);
        cantones_guanacaste = agregarOpcion(getResources().getStringArray(R.array.array_cantones_guanacaste), true);
        cantones_heredia = agregarOpcion(getResources().getStringArray(R.array.array_cantones_heredia), true);
        cantones_puntarenas = agregarOpcion(getResources().getStringArray(R.array.arrar_cantones_puntarenas), true);
        cantones_limon = agregarOpcion(getResources().getStringArray(R.array.array_cantones_limon), true);

        sp_cantones_por_provincia = (Spinner)findViewById(R.id.sp_cantones_id);

        sp_provincias = (Spinner)findViewById(R.id.sp_provincias_id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout, R.id.text, provincias);
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

    private void establecerAdaptadorSPCantones(String[] pLista)
    {
        ArrayAdapter<String> adapter_cant = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, pLista);
        sp_cantones_por_provincia.setAdapter(adapter_cant);
    }

    private String[] agregarOpcion(String[] pLista, Boolean esCanton)
    {
        List<String> list = new ArrayList<>(Arrays.asList(pLista));

        if (esCanton) { list.add(0, "Seleccione un canton"); }
        else { list.add(0, "Seleccione una provincia"); }

        return list.toArray(new String[list.size()]);
    }
}
