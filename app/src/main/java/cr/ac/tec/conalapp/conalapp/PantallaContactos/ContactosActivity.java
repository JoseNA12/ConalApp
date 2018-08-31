package cr.ac.tec.conalapp.conalapp.PantallaContactos;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cr.ac.tec.conalapp.conalapp.Adaptadores.PagerAdapterContactos;
import cr.ac.tec.conalapp.conalapp.R;

public class ContactosActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        inicializarComponentes();
    }

    private void inicializarComponentes()
    {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_id);
        tabLayout.addTab(tabLayout.newTab().setText("Seguridad"));
        tabLayout.addTab(tabLayout.newTab().setText("Salud"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_id);
        final PagerAdapterContactos adapter = new PagerAdapterContactos
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
    }
}
