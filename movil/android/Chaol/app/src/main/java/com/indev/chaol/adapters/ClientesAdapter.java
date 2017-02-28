package com.indev.chaol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.indev.chaol.R;
import com.indev.chaol.models.Clientes;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 14/01/2016.
 */
public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ViewHolder>{

    View.OnClickListener onClickListener;
    List<Clientes> clientes = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        Button btnEditar;
        Button btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNombre = (TextView) itemView.findViewById(R.id.item_nombre_cliente);
            btnEditar = (Button) itemView.findViewById(R.id.item_btn_editar_cliente);
            btnEliminar = (Button) itemView.findViewById(R.id.item_btn_eliminar_cliente);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Clientes getItemByPosition(int position) { return clientes.get(position);}

    public void addAll(List<Clientes> _clientes) { this.clientes.addAll(_clientes);}

    public void remove(int position) { this.clientes.remove(position);}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clientes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Clientes item = clientes.get(position);

        holder.txtNombre.setText(item.getNombre());
    }

    @Override
    public int getItemCount() {
        return clientes == null ? 0 : clientes.size();
    }

    public void removeItem(int position) {
        this.clientes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

}
