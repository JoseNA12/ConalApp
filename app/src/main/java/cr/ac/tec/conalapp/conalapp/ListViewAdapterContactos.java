package cr.ac.tec.conalapp.conalapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapterContactos
        extends ArrayAdapter<ContactosModelo> implements View.OnClickListener{

    private ArrayList<ContactosModelo> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tv_nombre_centro;
        TextView tv_provincia;
        TextView tv_numero_telefono;
        ImageView info;
    }

    public ListViewAdapterContactos(ArrayList<ContactosModelo> data, Context context) {
        super(context, R.layout.row_item_contactos, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        ContactosModelo contactos = (ContactosModelo)object;

        switch (v.getId())
        {
            case R.id.item_info:
                assert contactos != null;
                Snackbar.make(v, "Release date " + contactos.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        ContactosModelo contactos = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_contactos, parent, false);
            viewHolder.tv_nombre_centro = (TextView) convertView.findViewById(R.id.tv_nombre_centro_id);
            viewHolder.tv_provincia = (TextView) convertView.findViewById(R.id.tv_provincia_id);
            viewHolder.tv_numero_telefono = (TextView) convertView.findViewById(R.id.tv_numero_telefono_id);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        /*Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;*/

        viewHolder.tv_nombre_centro.setText(contactos.getNombreCentro());
        viewHolder.tv_provincia.setText(contactos.getType());
        viewHolder.tv_numero_telefono.setText(contactos.getNumeroTelefono());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
