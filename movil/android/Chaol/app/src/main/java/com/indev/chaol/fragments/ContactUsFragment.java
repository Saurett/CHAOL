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
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class ContactUsFragment extends Fragment {

    private FragmentManager fragmentManager;
    private FragmentTransaction mainFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        mainFragment = fragmentManager.beginTransaction();

        this.onPreRender();

        return view;
    }

    /**Carga los elementos iniciales del fragmento**/
    private void onPreRender() {
        this.onPreRenderSessionFragment();
    }

    /**Selecciona el fragmento a editar**/
    private void onPreRenderSessionFragment() {

        DecodeItem decodeItem = new DecodeItem();
        decodeItem.setIdView(R.id.menu_item_contact_us);

        DecodeExtraParams extraParams = new DecodeExtraParams();

        extraParams.setAccionFragmento(Constants.ACCION_VER);
        extraParams.setFragmentTag(Constants.ITEM_FRAGMENT.get(R.id.menu_item_contact_us));
        extraParams.setDecodeItem(decodeItem);

        getActivity().getIntent().putExtra(Constants.KEY_MAIN_DECODE,extraParams);

        mainFragment.replace(R.id.panel_contact_us_container, new ContactFragment(), Constants.ITEM_FRAGMENT.get(R.id.menu_item_contact_us));
        mainFragment.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
