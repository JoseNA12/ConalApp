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

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cr.ac.tec.conalapp.conalapp.ClaseSingleton;
import cr.ac.tec.conalapp.conalapp.Modelo.BoletinModelo;
import cr.ac.tec.conalapp.conalapp.PantallaCrearBoletin.CrearBoletinActivity;
import cr.ac.tec.conalapp.conalapp.R;

public class ListViewAdapterBoletin extends ArrayAdapter<BoletinModelo> implements View.OnClickListener {

    private ArrayList<BoletinModelo> dataSet;
    private Context mContext;

    private static class RetenedorVista {
        TextView tv_nombre_prefil;
        TextView tv_titular;
        TextView tv_provincia;
        TextView tv_fecha;
        TextView tv_hora;
        ImageView iv_gps;
        TextView tv_descripcion;
        Button btn_sospechosos;
        Button btn_comentarios;
    }

    public ListViewAdapterBoletin(ArrayList<BoletinModelo> data, Context context) {
        super(context, R.layout.row_item_boletines, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

        try
        {
            int position=(Integer) v.getTag();
            Object object= getItem(position);
            BoletinModelo boletin = (BoletinModelo)object;

            switch (v.getId())
            {
                case R.id.btn_sospechosos_id:
                    assert boletin != null;
                    Snackbar.make(v, "Sospechosos" , Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    break;

                case R.id.btn_comentarios_id:
                    assert boletin != null;
                    Snackbar.make(v, "Comentarios", Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    break;
            }
        }
        catch (ClassCastException e){ } // la imagen del gps no es seleccionable
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        BoletinModelo boletin = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ListViewAdapterBoletin.RetenedorVista retenedorVista; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            retenedorVista = new ListViewAdapterBoletin.RetenedorVista();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_boletines, parent, false);

            retenedorVista.tv_nombre_prefil = (TextView) convertView.findViewById(R.id.tv_nombre_prefil_id);
            retenedorVista.tv_titular = (TextView) convertView.findViewById(R.id.tv_titular_id);
            retenedorVista.tv_provincia = (TextView) convertView.findViewById(R.id.tv_provincia_id);
            retenedorVista.tv_fecha = (TextView) convertView.findViewById(R.id.tv_fecha_id);
            retenedorVista.tv_hora = (TextView) convertView.findViewById(R.id.tv_hora_id);
            retenedorVista.tv_descripcion = (TextView) convertView.findViewById(R.id.tv_descripcion_id);

            retenedorVista.iv_gps = (ImageView) convertView.findViewById(R.id.iv_gps_id);

            retenedorVista.btn_sospechosos = (Button) convertView.findViewById(R.id.btn_sospechosos_id);
            retenedorVista.btn_comentarios = (Button) convertView.findViewById(R.id.btn_comentarios_id);

            result = convertView;

            convertView.setTag(retenedorVista);
        }
        else
        {
            retenedorVista = (ListViewAdapterBoletin.RetenedorVista) convertView.getTag();
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

        retenedorVista.btn_sospechosos.setOnClickListener(this);
        retenedorVista.btn_sospechosos.setTag(position);
        retenedorVista.btn_comentarios.setOnClickListener(this);
        retenedorVista.btn_comentarios.setTag(position);

        retenedorVista.iv_gps.setOnClickListener(this);
        //retenedorVista.iv_gps.setTag(position);
        Glide.with(getContext())
                .load(boletin.getLinkImagenGPS())
                .fitCenter()
                .centerCrop()
                .into(retenedorVista.iv_gps);

        // Return the completed view to render on screen
        return convertView;
    }
}
