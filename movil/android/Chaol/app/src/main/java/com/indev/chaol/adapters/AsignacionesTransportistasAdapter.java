package com.indev.chaol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.indev.chaol.R;
import com.indev.chaol.fragments.AsignacionTransportistasFragment;
import com.indev.chaol.fragments.TransportistasFragment;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 14/01/2016.
 */
public class AsignacionesTransportistasAdapter extends RecyclerView.Adapter<AsignacionesTransportistasAdapter.ViewHolder> {

    View.OnClickListener onClickListener;
    List<Transportistas> transportistas = new ArrayList<>();


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        Button btnPerfil;
        Button btnAutorizar;
        Button btnAutorizado;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNombre = (TextView) itemView.findViewById(R.id.item_nombre_asignacion_transportista);
            btnPerfil = (Button) itemView.findViewById(R.id.item_btn_perfil_asignacion_transportista);
            btnAutorizar = (Button) itemView.findViewById(R.id.item_btn_autorizar_asinacion_transportista);
            btnAutorizado = (Button) itemView.findViewById(R.id.item_btn_autorizado_transportista);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Transportistas getItemByPosition(int position) {
        return transportistas.get(position);
    }

    public void addAll(List<Transportistas> _transportistas) {
        this.transportistas.addAll(_transportistas);
    }

    public void remove(int position) {
        this.transportistas.remove(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asignaciones_transportistas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Transportistas item = transportistas.get(position);
        /**Llenar el objeto que sera enviado al fragmento**/
        final DecodeItem decodeItem = new DecodeItem();

        decodeItem.setItemModel(item);
        decodeItem.setPosition(position);

        holder.txtNombre.setText(item.getNombre());

        holder.btnAutorizado.setVisibility(View.GONE);

        switch (item.getEstatus()) {
            case Constants.FB_KEY_ITEM_ESTATUS_TRANSPORTISTA_INTERESADO:
                holder.btnAutorizar.setVisibility(View.VISIBLE);
                break;
            case Constants.FB_KEY_ITEM_ESTATUS_TRANSPORTISTA_SELECCIONADO:
                holder.btnAutorizar.setVisibility(View.GONE);
                holder.btnAutorizado.setVisibility(View.VISIBLE);
                break;
            case Constants.FB_KEY_ITEM_ESTATUS_INACTIVO:
                holder.btnAutorizar.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        holder.btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decodeItem.setIdView(view.getId());
                AsignacionTransportistasFragment.onListenerAction(decodeItem);
            }
        });
        holder.btnAutorizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decodeItem.setIdView(view.getId());
                AsignacionTransportistasFragment.onListenerAction(decodeItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transportistas == null ? 0 : transportistas.size();
    }

    public void removeItem(int position) {
        this.transportistas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

}
