package cr.ac.tec.conalapp.conalapp.PantallaPrincipal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import cr.ac.tec.conalapp.conalapp.PantallaAboutUs.AboutUsActivity;
import cr.ac.tec.conalapp.conalapp.Adaptadores.PagerAdapterPrincipal;
import cr.ac.tec.conalapp.conalapp.PantallaConfiguracion.ConfiguracionActivity;
import cr.ac.tec.conalapp.conalapp.PantallaContactos.ContactosActivity;
import cr.ac.tec.conalapp.conalapp.PantallaLogin.IniciarSesionActivity;
import cr.ac.tec.conalapp.conalapp.PantallaPerfilUsuario.PerfilUsuarioActivity;
import cr.ac.tec.conalapp.conalapp.PantallaPuntosInteres.PuntosInteresActivity;
import cr.ac.tec.conalapp.conalapp.R;

public class PrincipalActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        inicializarComponentes();
    }

    private void inicializarComponentes()
    {
        //toolbar = (Toolbar) findViewById(R.id.toolbar); // quité la barra de la interfaz
        //setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_id);
        tabLayout.addTab(tabLayout.newTab().setText("Boletines"));
        tabLayout.addTab(tabLayout.newTab().setText("Reuniones"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_id);
        final PagerAdapterPrincipal adapter = new PagerAdapterPrincipal
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout_id);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView)findViewById(R.id.navigationView_id);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.mi_perfil:
                        startActivity(new Intent(getApplicationContext(), PerfilUsuarioActivity.class));
                        return true;

                    case R.id.puntos_interes:
                        startActivity(new Intent(getApplicationContext(), PuntosInteresActivity.class));
                        return true;

                    case R.id.contactos_emergencia:
                        startActivity(new Intent(getApplicationContext(), ContactosActivity.class));
                        return true;

                    case R.id.configuracion:
                        startActivity(new Intent(getApplicationContext(), ConfiguracionActivity.class));
                        return true;

                    case R.id.sobre_nosotros:
                        startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                        return true;

                    case R.id.cerrar_sesion:
                        startActivity(new Intent(getApplicationContext(), IniciarSesionActivity.class));
                        return true;

                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

}
