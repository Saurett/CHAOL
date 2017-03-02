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
import com.indev.chaol.utils.Constants;


/**
 * Created by saurett on 24/02/2017.
 */

public class ListadoTransportistasFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listado_transportistas, container, false);

        return view;
    }

    @Override
    public void onStart() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction mainFragment = fragmentManager.beginTransaction();
        mainFragment.add(R.id.listado_transportistas_container, new TransportistasFragment(), Constants.FRAGMENT_TRANSPORTISTAS);
        mainFragment.commit();

        super.onStart();
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
