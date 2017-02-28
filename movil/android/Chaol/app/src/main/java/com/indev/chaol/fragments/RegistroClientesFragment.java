package com.indev.chaol.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.indev.chaol.R;


/**
 * Created by saurett on 24/02/2017.
 */

public class RegistroClientesFragment extends Fragment {

    private static Button btnTitulo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registro_clientes, container, false);

        String titulo = getActivity().getTitle().toString();

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_clientes);
        btnTitulo.setText((titulo.equals(getString(R.string.default_item_menu_title_perfil)) ? "PERFIL" : "NUEVO"));


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "debe implementar");
        }
    }
}
