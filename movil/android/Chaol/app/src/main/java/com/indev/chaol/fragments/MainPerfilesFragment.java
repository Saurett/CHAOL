package com.indev.chaol.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indev.chaol.R;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class MainPerfilesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfiles, container, false);

        this.onPreRender();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction mainFragment = fragmentManager.beginTransaction();
        mainFragment.add(R.id.panel_perfiles_container, new PerfilClientesFragment(), Constants.FRAGMENT_MAIN_PERFIL);
        mainFragment.commit();

        return view;
    }

    private void onPreRender() {
        DecodeItem decodeItem = new DecodeItem();
        decodeItem.setIdView(R.id.menu_item_perfil);


        Clientes clientes = new Clientes();
        clientes.setNombre("Nombre de perfil se debo obtener en firebase");

        decodeItem.setItemModel(clientes);

        DecodeExtraParams extraParams = new DecodeExtraParams();

        extraParams.setAccionFragmento(Constants.ACCION_EDITAR);
        extraParams.setFragmentTag(Constants.ITEM_FRAGMENT.get(R.id.menu_item_perfil));
        extraParams.setDecodeItem(decodeItem);

        getActivity().getIntent().putExtra(Constants.KEY_MAIN_DECODE,extraParams);
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
