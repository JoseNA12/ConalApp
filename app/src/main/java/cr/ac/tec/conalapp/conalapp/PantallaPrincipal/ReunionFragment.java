package cr.ac.tec.conalapp.conalapp.PantallaPrincipal;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import cr.ac.tec.conalapp.conalapp.Adaptadores.ListViewAdapterBoletin;
import cr.ac.tec.conalapp.conalapp.Adaptadores.ListViewAdapterReunion;
import cr.ac.tec.conalapp.conalapp.Modelo.BoletinModelo;
import cr.ac.tec.conalapp.conalapp.Modelo.ReunionModelo;
import cr.ac.tec.conalapp.conalapp.PantallaCrearReunion.CrearReunionActivity;
import cr.ac.tec.conalapp.conalapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReunionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeLayout;
    private View view;

    private ArrayList<ReunionModelo> array_reuniones;
    private ListView listView;
    private static ListViewAdapterReunion adapter;

    private FloatingActionButton floatingActionButtonCrearReunion;

    public ReunionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reunion, container, false);
        inicializarComponentes(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void inicializarComponentes(View pView)
    {
        initSwipeLayout(pView);
        initListView(pView);
        initFloatingActiobButton(pView);
    }

    private void initSwipeLayout(View pView)
    {
        swipeLayout = (SwipeRefreshLayout) pView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_blue_dark);
    }

    private void initListView(View pView)
    {
        listView = (ListView) view.findViewById(R.id.lv_reuniones_id);

        array_reuniones = new ArrayList<>();
        array_reuniones.add(new ReunionModelo("El brayan", "Asalto 2 heridos gravedad, delicuentes huyeron", "Cartago", "30/8/2018", "09:53 p.m", "Esta es la descripción del acontecimiento, tengan cuidado no anden solos."));
        array_reuniones.add(new ReunionModelo("El Suarez", "Vagabundo amigo de lo ajeno", "Cartago", "30/8/2018", "10:30 a.m", "Esta es la descripción del acontecimiento, tengan cuidado no anden solos."));
        array_reuniones.add(new ReunionModelo("Paisita carepicha", "Alerta de ladron", "Guanacaste", "30/8/2018", "12:53 p.m", "Esta es la descripción del acontecimiento, tengan cuidado no anden solos."));

        adapter = new ListViewAdapterReunion(array_reuniones, getContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ReunionModelo dataModel = array_reuniones.get(position);

                Snackbar.make(view, "Ayyy papitoooohh", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }

    private void initFloatingActiobButton(View pView)
    {
        floatingActionButtonCrearReunion = (FloatingActionButton) pView.findViewById(R.id.fab_crear_reunion);
        floatingActionButtonCrearReunion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CrearReunionActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * As a trick, you may be found interesting to disable manual swipe gesture,
     * maybe temporarily or because you only want to show progress animation programmatically.
     * What you need to do is to use the method setEnabled() and set it to false.
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 5000);
    }

}
