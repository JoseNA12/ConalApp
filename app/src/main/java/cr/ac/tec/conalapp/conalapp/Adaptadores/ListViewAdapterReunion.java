package cr.ac.tec.conalapp.conalapp.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cr.ac.tec.conalapp.conalapp.Modelo.ReunionModelo;
import cr.ac.tec.conalapp.conalapp.R;

public class ListViewAdapterReunion extends ArrayAdapter<ReunionModelo> implements View.OnClickListener {

    private ArrayList<ReunionModelo> dataSet;
    private Context mContext;

    private static class RetenedorVista {
        TextView tv_nombre_prefil;
        TextView tv_titular;
        TextView tv_provincia;
        TextView tv_fecha;
        TextView tv_hora;
        ImageView iv_gps;
        TextView tv_descripcion;

        Button btn_comentarios;
    }

    public ListViewAdapterReunion(ArrayList<ReunionModelo> data, Context context) {
        super(context, R.layout.row_item_reuniones, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        ReunionModelo boletin = (ReunionModelo)object;

        switch (v.getId())
        {
            case R.id.btn_comentarios_id:
                assert boletin != null;
                Snackbar.make(v, "Comentarios", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        ReunionModelo boletin = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ListViewAdapterReunion.RetenedorVista retenedorVista; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            retenedorVista = new ListViewAdapterReunion.RetenedorVista();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_reuniones, parent, false);

            retenedorVista.tv_nombre_prefil = (TextView) convertView.findViewById(R.id.tv_nombre_prefil_id);
            retenedorVista.tv_titular = (TextView) convertView.findViewById(R.id.tv_titular_id);
            retenedorVista.tv_provincia = (TextView) convertView.findViewById(R.id.tv_provincia_id);
            retenedorVista.tv_fecha = (TextView) convertView.findViewById(R.id.tv_fecha_id);
            retenedorVista.tv_hora = (TextView) convertView.findViewById(R.id.tv_hora_id);
            retenedorVista.tv_descripcion = (TextView) convertView.findViewById(R.id.tv_descripcion_id);

            retenedorVista.iv_gps = (ImageView) convertView.findViewById(R.id.iv_gps_id);

            retenedorVista.btn_comentarios = (Button) convertView.findViewById(R.id.btn_comentarios_id);

            result = convertView;

            convertView.setTag(retenedorVista);
        }
        else
        {
            retenedorVista = (ListViewAdapterReunion.RetenedorVista) convertView.getTag();
            result = convertView;
        }

        /*Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;*/

        retenedorVista.tv_nombre_prefil.setText(boletin.getAutor());
        retenedorVista.tv_titular.setText(boletin.getTitular());
        retenedorVista.tv_provincia.setText(boletin.getProvincia());
        retenedorVista.tv_fecha.setText(boletin.getFecha());
        retenedorVista.tv_hora.setText(boletin.getHora());
        retenedorVista.tv_descripcion.setText(boletin.getDescripcion());

        retenedorVista.btn_comentarios.setOnClickListener(this);
        retenedorVista.btn_comentarios.setTag(position);

        retenedorVista.iv_gps.setOnClickListener(this);
        retenedorVista.iv_gps.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }
}
