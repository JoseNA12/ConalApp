package cr.ac.tec.conalapp.conalapp.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cr.ac.tec.conalapp.conalapp.Modelo.ComunidadModelo;
import cr.ac.tec.conalapp.conalapp.R;

public class ListViewAdapterBusquedaComunidades extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<ComunidadModelo> listaComunidades = null;
    private ArrayList<ComunidadModelo> arraylist;

    public ListViewAdapterBusquedaComunidades(Context context, List<ComunidadModelo> listaComunidades)
    {
        mContext = context;
        this.listaComunidades = listaComunidades;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<ComunidadModelo>();
        this.arraylist.addAll(listaComunidades);
    }

    public class ViewHolder
    {
        TextView tv_nombre_comunidad;
        TextView tv_provincia;
        TextView tv_canton;
    }

    @Override
    public int getCount() {
        return listaComunidades.size();
    }

    @Override
    public ComunidadModelo getItem(int position) {
        return listaComunidades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent)
    {
        final ViewHolder holder;
        if (view == null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_item_busqueda_comunidad, null);
            // Locate the TextViews in listview_item.xml
            holder.tv_nombre_comunidad = (TextView) view.findViewById(R.id.tv_nombre_comunidad_id);
            holder.tv_provincia = (TextView) view.findViewById(R.id.tv_provincia_id);
            holder.tv_canton = (TextView) view.findViewById(R.id.tv_canton_id);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.tv_nombre_comunidad.setText(listaComunidades.get(position).getNombre());
        holder.tv_provincia.setText(listaComunidades.get(position).getProvincia());
        holder.tv_canton.setText(listaComunidades.get(position).getCanton());
        return view;
    }

    // Filter Class
    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        listaComunidades.clear();

        if (charText.length() == 0)
        {
            listaComunidades.addAll(arraylist);
        }
        else {
            for (ComunidadModelo wp : arraylist)
            {
                if (wp.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) {
                    listaComunidades.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
