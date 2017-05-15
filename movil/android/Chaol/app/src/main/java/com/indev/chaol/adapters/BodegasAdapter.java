package com.indev.chaol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.indev.chaol.R;
import com.indev.chaol.fragments.BodegasFragment;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.DecodeItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 14/01/2016.
 */
public class BodegasAdapter extends RecyclerView.Adapter<BodegasAdapter.ViewHolder>{

    View.OnClickListener onClickListener;
    List<Bodegas> bodegas = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        Button btnEditar;
        Button btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNombre = (TextView) itemView.findViewById(R.id.item_nombre_bodega);
            btnEditar = (Button) itemView.findViewById(R.id.item_btn_editar_bodega);
            btnEliminar = (Button) itemView.findViewById(R.id.item_btn_eliminar_bodega);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Bodegas getItemByPosition(int position) { return bodegas.get(position);}

    public void addAll(List<Bodegas> _bodegas) { this.bodegas.addAll(_bodegas);}

    public void remove(int position) { this.bodegas.remove(position);}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bodegas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Bodegas item = bodegas.get(position);
        /**Llenar el objeto que sera enviado al fragmento**/
        final DecodeItem decodeItem = new DecodeItem();

        decodeItem.setItemModel(item);
        decodeItem.setPosition(position);

        holder.txtNombre.setText(item.getNombreDeLaBodega());
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decodeItem.setIdView(view.getId());
                BodegasFragment.onListenerAction(decodeItem);
            }
        });
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decodeItem.setIdView(view.getId());
                BodegasFragment.onListenerAction(decodeItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bodegas == null ? 0 : bodegas.size();
    }

    public void removeItem(int position) {
        this.bodegas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

}
