package com.indev.chaol.adapters;

import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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
import com.indev.chaol.utils.Constants;

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

        int color = R.color.colorPanelFletesUno;

        switch (item.getEstatus()) {
            case Constants.FB_KEY_ITEM_STATUS_FLETE_POR_COTIZAR:
                color = R.color.colorPanelFletesUno;
                break;
            case Constants.FB_KEY_ITEM_STATUS_ESPERANDO_POR_TRANSPORTISTA:
                color = R.color.colorPanelFletesDos;
                break;
            case Constants.FB_KEY_ITEM_STATUS_TRANSPORTISTA_POR_CONFIRMAR:
                color = R.color.colorPanelFletesTres;
                break;
            case Constants.FB_KEY_ITEM_STATUS_UNIDADES_POR_ASIGNAR:
                color = R.color.colorPanelFletesCuatro;
                break;
            case Constants.FB_KEY_ITEM_STATUS_ENVIO_POR_INICIAR:
                color = R.color.colorPanelFletesCinco;
                break;
            case Constants.FB_KEY_ITEM_STATUS_EN_PROGRESO:
                color = R.color.colorPanelFletesSeis;
                break;
            case Constants.FB_KEY_ITEM_STATUS_ENTREGADO:
                color = R.color.colorPanelFletesSiete;
                break;
            case Constants.FB_KEY_ITEM_STATUS_FINALIZADO:
                color = R.color.colorPanelFletesOcho;
                break;
            case Constants.FB_KEY_ITEM_STATUS_CANCELADO:
                color = R.color.colorPanelFletesNueve;
                break;
            default:
                break;
        }

        holder.fabStatus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat
                .getColor(holder.fabStatus.getContext(), color)));

        holder.txtEstadoAgenda.setText(Constants.TITLE_ESTATUS_FLETES.get(item.getEstatus()));
        holder.txtNombreCliente.setText(item.getNombre());
        holder.txtNombreTransportista.setText(item.getNombreDelTransportista());

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
