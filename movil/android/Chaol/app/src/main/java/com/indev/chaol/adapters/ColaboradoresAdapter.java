package com.indev.chaol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.indev.chaol.R;
import com.indev.chaol.fragments.ColaboradoresFragment;
import com.indev.chaol.models.Administradores;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saurett on 14/01/2016.
 */
public class ColaboradoresAdapter extends RecyclerView.Adapter<ColaboradoresAdapter.ViewHolder>{

    View.OnClickListener onClickListener;
    List<Administradores> colaboradores = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        Switch switchActivar;
        Button btnEditar;
        Button btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNombre = (TextView) itemView.findViewById(R.id.item_nombre_colaborador);
            switchActivar = (Switch) itemView.findViewById(R.id.item_switch_activar_colaborador);
            btnEditar = (Button) itemView.findViewById(R.id.item_btn_editar_colaboradores);
            btnEliminar = (Button) itemView.findViewById(R.id.item_btn_eliminar_colaborador);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Administradores getItemByPosition(int position) { return colaboradores.get(position);}

    public void addAll(List<Administradores> _administradores) { this.colaboradores.addAll(_administradores);}

    public void remove(int position) { this.colaboradores.remove(position);}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_colaboradores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Administradores item = colaboradores.get(position);
        /**Llenar el objeto que sera enviado al fragmento**/
        final DecodeItem decodeItem = new DecodeItem();

        decodeItem.setItemModel(item);
        decodeItem.setPosition(position);

        holder.switchActivar.setChecked((item.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ACTIVO)));

        holder.switchActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = ((Switch) view).isChecked();

                item.setEstatus((check) ? Constants.FB_KEY_ITEM_ESTATUS_ACTIVO : Constants.FB_KEY_ITEM_ESTATUS_INACTIVO);

                decodeItem.setIdView(view.getId());
                decodeItem.setItemModel(item);
                ColaboradoresFragment.onListenerAction(decodeItem);
            }
        });

        holder.txtNombre.setText(item.getNombre());
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decodeItem.setIdView(view.getId());
                ColaboradoresFragment.onListenerAction(decodeItem);
            }
        });
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decodeItem.setIdView(view.getId());
                ColaboradoresFragment.onListenerAction(decodeItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colaboradores == null ? 0 : colaboradores.size();
    }

    public void removeItem(int position) {
        this.colaboradores.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

}
