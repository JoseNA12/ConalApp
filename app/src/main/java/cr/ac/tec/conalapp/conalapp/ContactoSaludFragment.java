package cr.ac.tec.conalapp.conalapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactoSaludFragment extends Fragment {

    private ArrayList<ContactosModelo> array_contactos;
    private ListView listView;
    private static ListViewAdapterContactos adapter;

    public ContactoSaludFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacto_salud, container, false);

        inicializarComponentes(view);

        return view;
    }

    private void inicializarComponentes(View view)
    {
        listView = (ListView)view.findViewById(R.id.list);

        array_contactos = leerArchivoContactos();
        adapter = new ListViewAdapterContactos(array_contactos, getContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ContactosModelo dataModel = array_contactos.get(position);

                Snackbar.make(view, "Tels: " + dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }

    private ArrayList<ContactosModelo> leerArchivoContactos()
    {
        LeerArchivoContactos miLector = null;

        try
        {
            miLector = new LeerArchivoContactos(getActivity().getAssets().open(ClaseSingleton.archContactosSalud));
        }
        catch (IOException e) { e.printStackTrace(); }

        return miLector.array_contactos;
    }
}
