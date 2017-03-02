package com.indev.chaol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.indev.chaol.R;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.Transportistas;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 14/01/2016.
 */
public class ChoferesAdapter extends RecyclerView.Adapter<ChoferesAdapter.ViewHolder>{

    View.OnClickListener onClickListener;
    List<Choferes> choferes = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        Button btnEditar;
        Button btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNombre = (TextView) itemView.findViewById(R.id.item_nombre_chofer);
            btnEditar = (Button) itemView.findViewById(R.id.item_btn_editar_chofer);
            btnEliminar = (Button) itemView.findViewById(R.id.item_btn_eliminar_chofer);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Choferes getItemByPosition(int position) { return choferes.get(position);}

    public void addAll(List<Choferes> _choferes) { this.choferes.addAll(_choferes);}

    public void remove(int position) { this.choferes.remove(position);}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choferes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Choferes item = choferes.get(position);

        holder.txtNombre.setText(item.getNombre());
    }

    @Override
    public int getItemCount() {
        return choferes == null ? 0 : choferes.size();
    }

    public void removeItem(int position) {
        this.choferes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

}