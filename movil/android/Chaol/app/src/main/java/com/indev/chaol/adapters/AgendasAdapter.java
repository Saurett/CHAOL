package com.indev.chaol.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indev.chaol.R;
import com.indev.chaol.fragments.AgendasFragment;
import com.indev.chaol.models.Agendas;
import com.indev.chaol.models.DecodeItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 14/01/2016.
 */
public class AgendasAdapter extends RecyclerView.Adapter<AgendasAdapter.ViewHolder> {

    View.OnClickListener onClickListener;
    List<Agendas> agendas = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        FloatingActionButton fabStatus;
        TextView txtEstadoAgenda;
        TextView txtNombreCliente;
        TextView txtNombreTransportista;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.item_color_agenda);
            fabStatus = (FloatingActionButton) itemView.findViewById(R.id.item_fab_status_agenda);
            txtEstadoAgenda = (TextView) itemView.findViewById(R.id.item_estado_agenda);
            txtNombreCliente = (TextView) itemView.findViewById(R.id.item_nombre_cliente_agenda);
            txtNombreTransportista = (TextView) itemView.findViewById(R.id.item_nombre_transportista_agenda);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Agendas getItemByPosition(int position) {
        return agendas.get(position);
    }

    public void addAll(List<Agendas> _agendas) {
        this.agendas.addAll(_agendas);
    }

    public void remove(int position) {
        this.agendas.remove(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Agendas item = agendas.get(position);
        /**Llenar el objeto que sera enviado al fragmento**/
        final DecodeItem decodeItem = new DecodeItem();

        decodeItem.setItemModel(item);
        decodeItem.setPosition(position);

        int color = Color.GREEN;

        switch (item.getStatus()) {
            case 1:
                color = Color.BLUE;
                break;
            case 2:
                color = Color.CYAN;
                break;
            case 3:
                color = Color.YELLOW;
                break;
            case 4:
                color = Color.GREEN;
                break;
        }

        holder.fabStatus.setBackgroundTintList(ColorStateList.valueOf(color));

        holder.txtEstadoAgenda.setText(item.getEstado());
        holder.txtNombreCliente.setText(item.getNombreCliente());
        holder.txtNombreTransportista.setText(item.getNombreTransportista());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decodeItem.setIdView(v.getId());
                AgendasFragment.onListenerAction(decodeItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return agendas == null ? 0 : agendas.size();
    }

    public void removeItem(int position) {
        this.agendas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

}
