package com.indev.chaol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.indev.chaol.R;
import com.indev.chaol.fragments.TractoresFragment;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Tractores;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 14/01/2016.
 */
public class TractoresAdapter extends RecyclerView.Adapter<TractoresAdapter.ViewHolder>{

    View.OnClickListener onClickListener;
    List<Tractores> tractores = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNumEconomico;
        Button btnEditar;
        Button btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNumEconomico = (TextView) itemView.findViewById(R.id.item_num_economico_tractor);
            btnEditar = (Button) itemView.findViewById(R.id.item_btn_editar_tractor);
            btnEliminar = (Button) itemView.findViewById(R.id.item_btn_eliminar_tractor);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Tractores getItemByPosition(int position) { return tractores.get(position);}

    public void addAll(List<Tractores> _tractores) { this.tractores.addAll(_tractores);}

    public void remove(int position) { this.tractores.remove(position);}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tractores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Tractores item = tractores.get(position);
        /**Llenar el objeto que sera enviado al fragmento**/
        final DecodeItem decodeItem = new DecodeItem();

        decodeItem.setItemModel(item);
        decodeItem.setPosition(position);

        holder.txtNumEconomico.setText(item.getNumEconomico());
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decodeItem.setIdView(view.getId());
                TractoresFragment.onListenerAction(decodeItem);
            }
        });
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decodeItem.setIdView(view.getId());
                TractoresFragment.onListenerAction(decodeItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tractores == null ? 0 : tractores.size();
    }

    public void removeItem(int position) {
        this.tractores.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

}
