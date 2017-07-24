package com.indev.chaol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.indev.chaol.R;
import com.indev.chaol.fragments.ChoferesFragment;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.utils.Constants;

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
        Switch switchActivar;
        Button btnEditar;
        Button btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNombre = (TextView) itemView.findViewById(R.id.item_nombre_chofer);
            switchActivar = (Switch) itemView.findViewById(R.id.item_switch_activar_chofer);
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
        /**Llenar el objeto que sera enviado al fragmento**/
        final DecodeItem decodeItem = new DecodeItem();

        decodeItem.setItemModel(item);
        decodeItem.setPosition(position);

        boolean checked = (item.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_LIBRE) || item.getEstatus().equals(Constants.FB_KEY_ITEM_ESTATUS_ASIGNADO));

        holder.switchActivar.setChecked(checked);

        holder.switchActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = ((Switch) view).isChecked();

                item.setEstatus((check) ? Constants.FB_KEY_ITEM_ESTATUS_LIBRE : Constants.FB_KEY_ITEM_ESTATUS_INACTIVO);

                decodeItem.setIdView(view.getId());
                decodeItem.setItemModel(item);
                ChoferesFragment.onListenerAction(decodeItem);
            }
        });


        holder.txtNombre.setText(item.getNombre());
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decodeItem.setIdView(view.getId());
                ChoferesFragment.onListenerAction(decodeItem);
            }
        });
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decodeItem.setIdView(view.getId());
                ChoferesFragment.onListenerAction(decodeItem);
            }
        });
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
