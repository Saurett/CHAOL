package com.indev.chaol.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indev.chaol.R;


/**
 * Created by saurett on 24/02/2017.
 */

public class MainPanelFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_panel, container, false);

        /*
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction mainFragment = fragmentManager.beginTransaction();
        mainFragment.add(R.id.fragment_main_container, new MainPanelFragment(), Constans.FRAGMENT_MAIN_PANEL);
        mainFragment.commit();
        */

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
