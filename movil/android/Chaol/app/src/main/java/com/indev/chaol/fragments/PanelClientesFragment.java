package com.indev.chaol.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.indev.chaol.R;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;


/**
 * Created by saurett on 24/02/2017.
 */

public class PanelClientesFragment extends Fragment implements View.OnClickListener {

    private Button btnTitulo;
    private static NavigationDrawerInterface activityInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_panel_clientes, container, false);

        btnTitulo = (Button) view.findViewById(R.id.btn_titulo_clientes);

        btnTitulo.setOnClickListener(this);

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
            activityInterface = (NavigationDrawerInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "debe implementar");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_titulo_clientes:
                activityInterface.onChangeMainFragment(R.id.menu_item_clientes);
                break;
        }
    }
}
