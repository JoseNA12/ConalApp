package cr.ac.tec.conalapp.conalapp.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cr.ac.tec.conalapp.conalapp.Modelo.PuntosInteresModelo;
import cr.ac.tec.conalapp.conalapp.R;

public class ListViewAdapterPuntosInteres extends ArrayAdapter<PuntosInteresModelo> implements View.OnClickListener {

    private ArrayList<PuntosInteresModelo> dataSet;
    private Context mContext;
    //private int lastPosition = -1;

    // View lookup cache
    private static class RetenedorVista {
        TextView tv_provincia;
        TextView tv_canton;
        ImageButton ibtn_borrar_punto;
    }

    public ListViewAdapterPuntosInteres(ArrayList<PuntosInteresModelo> data, Context context) {
        super(context, R.layout.row_item_punto_interes, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        PuntosInteresModelo puntosInteres = (PuntosInteresModelo) object;

        switch (v.getId()) {
            case R.id.ibtn_borrar_punto_id:
                assert puntosInteres != null;

                break;
        }
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        PuntosInteresModelo puntosInteresModelo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ListViewAdapterPuntosInteres.RetenedorVista retenedorVista; // view lookup cache stored in tag

        final View result;

        if (convertView == null)
        {
            retenedorVista = new ListViewAdapterPuntosInteres.RetenedorVista();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_punto_interes, parent, false);

            retenedorVista.tv_provincia = (TextView) convertView.findViewById(R.id.tv_provincia_id);
            retenedorVista.tv_canton = (TextView) convertView.findViewById(R.id.tv_canton_id);

            retenedorVista.ibtn_borrar_punto = (ImageButton) convertView.findViewById(R.id.ibtn_borrar_punto_id);

            result = convertView;

            convertView.setTag(retenedorVista);
        }
        else
        {
            retenedorVista = (ListViewAdapterPuntosInteres.RetenedorVista) convertView.getTag();
            result = convertView;
        }

        /*Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;*/

        retenedorVista.tv_provincia.setText(puntosInteresModelo.getProvincia());
        retenedorVista.tv_canton.setText(puntosInteresModelo.getCanton());
        retenedorVista.ibtn_borrar_punto.setTag(position);
        retenedorVista.ibtn_borrar_punto.setOnClickListener(this);

        // Return the completed view to render on screen
        return convertView;
    }
}